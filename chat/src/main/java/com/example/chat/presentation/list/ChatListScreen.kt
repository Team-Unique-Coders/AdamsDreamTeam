package com.example.chat.presentation.list

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chat.navigation.ChatRoutes
import com.example.chat.presentation.ui.ChatListItem
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.offset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(nav: NavController, vm: ChatListViewModel) {
    val state by vm.state.collectAsState()
    var query by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Chat") }) }
    ) { padding ->
        Column(Modifier.padding(padding)) {

            TabRow(selectedTabIndex = 0) {
                Tab(selected = true, onClick = {}) { Text("Chats", Modifier.padding(16.dp)) }
                Tab(selected = false, onClick = { nav.navigate(ChatRoutes.CONTACTS) }) {
                    Text("Contacts", Modifier.padding(16.dp))
                }
                Tab(selected = false, onClick = {}) { Text("Calls", Modifier.padding(16.dp)) }
            }

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search") },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                singleLine = true
            )

            if (state.isLoading) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.chats) { chat ->
                    ChatListItem(chat = chat) {
                        nav.navigate(ChatRoutes.detail(chat.id))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListPane(
    nav: NavController,
    vm: ChatListViewModel,
    query: String = ""
) {
    val state by vm.state.collectAsState()

    val filtered = remember(state.chats, query) {
        val q = query.trim().lowercase()
        if (q.isEmpty()) state.chats
        else state.chats.filter { item ->
            item.title.lowercase().contains(q) || item.lastMessage.lowercase().contains(q)
        }
    }

    if (state.isLoading) {
        LinearProgressIndicator(Modifier.fillMaxWidth())
    }

    val density = LocalDensity.current
    val revealWidth = 88.dp                       // how much of the tray is revealed
    val revealPx = with(density) { revealWidth.toPx() }
    val cardShape = MaterialTheme.shapes.medium

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filtered, key = { it.id }) { chat ->
            val scope = rememberCoroutineScope()
            val offsetX = remember { Animatable(0f) }  // 0f = closed, -revealPx = open
            var showConfirm by rememberSaveable { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(revealPx) {
                        detectHorizontalDragGestures(
                            onHorizontalDrag = { _, dragAmount ->
                                // dragAmount: +right, -left. We only allow dragging left.
                                val new = (offsetX.value + dragAmount)
                                    .coerceIn(-revealPx, 0f)
                                scope.launch { offsetX.snapTo(new) }
                            },
                            onDragEnd = {
                                // settle at half-way threshold
                                val target = if (offsetX.value <= -revealPx / 2f) -revealPx else 0f
                                scope.launch { offsetX.animateTo(target, tween(180)) }
                            },
                            onDragCancel = {
                                scope.launch { offsetX.animateTo(0f, tween(180)) }
                            }
                        )
                    }
            ) {
                // Orange delete tray (stays underneath)
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

                // Foreground row; slides horizontally with the drag
                Box(
                    modifier = Modifier.offset { IntOffset(offsetX.value.roundToInt(), 0) }
                ) {
                    ChatListItem(chat = chat) {
                        // Close before navigating if it’s half open
                        scope.launch { offsetX.animateTo(0f, tween(160)) }
                        nav.navigate(ChatRoutes.detail(chat.id))
                    }
                }
            }

            // Per-row confirmation dialog
            if (showConfirm) {
                AlertDialog(
                    onDismissRequest = {
                        showConfirm = false
                        scope.launch { offsetX.animateTo(0f, tween(160)) }
                    },
                    title = { Text("Delete chat?") },
                    text = { Text("This will remove the conversation from the list. This can’t be undone.") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showConfirm = false
                                vm.deleteChat(chat.id) // row disappears from list
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
