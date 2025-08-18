package com.example.chat.presentation.calls

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallMissed
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chat.domain.model.CallLog
import com.example.chat.domain.model.CallType
import com.example.chat.domain.repository.ChatRepository
import com.example.chat.navigation.ChatRoutes
import com.example.chat.presentation.ui.Avatar
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


data class CallItemUi(
    val id: String,
    val contactId: String,
    val name: String,
    val timestamp: Long,
    val durationSec: Int,
    val type: CallType,
    val isVideo: Boolean,
    val avatarUrl: String?
)

private data class DaySection(val header: String, val items: List<CallItemUi>)

data class CallsUiState(
    val items: List<CallItemUi> = emptyList(),
    val isLoading: Boolean = true
)


class CallsViewModel(private val repo: ChatRepository) : ViewModel() {

    val state = combine(repo.calls(), repo.contacts()) { calls, contacts ->
        val items = calls.sortedByDescending { it.timestamp }.map { c ->
            val contact = contacts.firstOrNull { it.id == c.contactId }
            CallItemUi(
                id = c.id,
                contactId = c.contactId,
                name = contact?.name ?: "Unknown",
                timestamp = c.timestamp,
                durationSec = c.durationSec,
                type = c.type,
                isVideo = c.isVideo,
                avatarUrl = contact?.avatarUrl
            )
        }
        CallsUiState(items = items, isLoading = false)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CallsUiState())

    fun deleteCall(callId: String) {
        viewModelScope.launch { repo.deleteCall(callId) }
    }

    companion object {
        fun factory(repo: ChatRepository) = viewModelFactory {
            initializer { CallsViewModel(repo) }
        }
    }
}


@Composable
fun CallsPane(
    nav: NavController,
    vm: CallsViewModel,
    query: String = ""
) {
    val state by vm.state.collectAsState()

    if (state.isLoading) {
        LinearProgressIndicator(Modifier.fillMaxWidth())
    }

    val filtered = remember(state.items, query) {
        val q = query.trim().lowercase()
        if (q.isEmpty()) state.items
        else {
            val typeMatch: (CallType) -> Boolean = when (q) {
                "missed"   -> { t -> t == CallType.MISSED }
                "incoming" -> { t -> t == CallType.INCOMING }
                "outgoing" -> { t -> t == CallType.OUTGOING }
                else       -> { _ -> false }
            }
            state.items.filter { item ->
                item.name.lowercase().contains(q) || typeMatch(item.type)
            }
        }
    }

    val sections = remember(filtered) { groupByDay(filtered) }

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        sections.forEach { section ->
            // SECTION HEADER (Today / Yesterday / dd MMMM yyyy)
            item(key = "header_${section.header}") {
                Text(
                    text = section.header,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp, bottom = 6.dp)
                )
                Divider(Modifier.padding(bottom = 8.dp))
            }

            // SECTION ROWS (keeps your half-reveal + confirm delete)
            items(section.items, key = { it.id }) { item ->
                val scope = rememberCoroutineScope()
                val density = LocalDensity.current
                val revealWidth = 88.dp
                val revealPx = with(density) { revealWidth.toPx() }
                val cardShape = MaterialTheme.shapes.medium
                var showConfirm by rememberSaveable { mutableStateOf(false) }
                val offsetX = remember { Animatable(0f) }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(revealPx) {
                            detectHorizontalDragGestures(
                                onHorizontalDrag = { _, dragAmount ->
                                    val new = (offsetX.value + dragAmount).coerceIn(-revealPx, 0f)
                                    scope.launch { offsetX.snapTo(new) }
                                },
                                onDragEnd = {
                                    val target = if (offsetX.value <= -revealPx / 2f) -revealPx else 0f
                                    scope.launch { offsetX.animateTo(target, tween(180)) }
                                },
                                onDragCancel = {
                                    scope.launch { offsetX.animateTo(0f, tween(180)) }
                                }
                            )
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(cardShape)
                            .background(Color(0xFFFF7A00)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.weight(1f))
                        IconButton(
                            onClick = { showConfirm = true },
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Delete",
                                tint = Color.White
                            )
                        }
                    }

                    Box(
                        modifier = Modifier.offset { IntOffset(offsetX.value.roundToInt(), 0) }
                    ) {
                        CallRow(
                            item = item,
                            onClick = {
                                scope.launch { offsetX.animateTo(0f, tween(160)) }
                                nav.navigate(ChatRoutes.detail("chat_${item.contactId}"))
                            }
                        )
                    }
                }

                if (showConfirm) {
                    AlertDialog(
                        onDismissRequest = {
                            showConfirm = false
                            scope.launch { offsetX.animateTo(0f, tween(160)) }
                        },
                        title = { Text("Delete call?") },
                        text = { Text("This will remove the call log entry. This can’t be undone.") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showConfirm = false
                                    vm.deleteCall(item.id)
                                }
                            ) { Text("Delete", color = MaterialTheme.colorScheme.error) }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    showConfirm = false
                                    scope.launch { offsetX.animateTo(0f, tween(160)) }
                                }
                            ) { Text("Cancel") }
                        }
                    )
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    }

}

@Composable
private fun CallRow(item: CallItemUi, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        ListItem(
            leadingContent = { Avatar(name = item.name, url = item.avatarUrl, size = 40.dp) },
            headlineContent = { Text(item.name) },
            supportingContent = {
                val time = timeOfDay(item.timestamp)
                val dur = formatDuration(item.durationSec, item.type)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DirectionIcon(type = item.type)
                    Spacer(Modifier.width(6.dp))
                    Text("$time${if (dur.isNotEmpty()) " • $dur" else ""}")
                }
            },
            trailingContent = {
                IconButton(onClick = onClick) {
                    val ico = if (item.isVideo) Icons.Filled.Videocam else Icons.Filled.Call
                    Icon(
                        ico,
                        contentDescription = if (item.isVideo) "Video call" else "Call",
                        tint = Color(0xFF2E7D32)
                    )
                }
            }
        )
    }
}

@Composable
private fun DirectionIcon(type: CallType) {
    val (icon, tint) = when (type) {
        CallType.INCOMING -> Icons.Filled.CallReceived to Color(0xFF2E7D32)
        CallType.OUTGOING -> Icons.Filled.CallMade     to Color(0xFFF57C00)
        CallType.MISSED  -> Icons.Filled.CallMissed    to Color(0xFFD32F2F)
    }
    Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
}


private fun formatDuration(sec: Int, type: CallType): String {
    if (type == CallType.MISSED || sec <= 0) return ""
    val m = sec / 60
    val s = sec % 60
    return if (m > 0) "${m}m ${s}s" else "${s}s"
}

private fun timeOfDay(ts: Long): String =
    SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(ts))

private fun groupByDay(items: List<CallItemUi>): List<DaySection> {
    if (items.isEmpty()) return emptyList()
    val todayCal = Calendar.getInstance()
    val yestCal = (todayCal.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -1) }

    fun headerFor(ts: Long): String {
        val cal = Calendar.getInstance().apply { timeInMillis = ts }
        return when {
            sameDay(cal, todayCal) -> "Today"
            sameDay(cal, yestCal) -> "Yesterday"
            else -> SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date(ts))
        }
    }

    return items
        .groupBy { headerFor(it.timestamp) }
        .map { (header, calls) -> DaySection(header, calls.sortedByDescending { it.timestamp }) }
        .sortedByDescending { section -> section.items.firstOrNull()?.timestamp ?: 0L }
}

private fun sameDay(a: Calendar, b: Calendar): Boolean =
    a.get(Calendar.YEAR) == b.get(Calendar.YEAR) &&
            a.get(Calendar.DAY_OF_YEAR) == b.get(Calendar.DAY_OF_YEAR)
