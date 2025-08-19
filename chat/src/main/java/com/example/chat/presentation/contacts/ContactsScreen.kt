package com.example.chat.presentation.contacts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chat.navigation.ChatRoutes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import com.example.chat.domain.model.Contact
import kotlinx.coroutines.launch
import com.example.chat.presentation.ui.Avatar
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(nav: NavController, vm: ContactsViewModel) {
    val state by vm.state.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Contacts") }) }) { padding ->
        if (state.isLoading) {
            LinearProgressIndicator(Modifier.fillMaxWidth())
        }
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(state.contacts) { contact ->
                ElevatedCard(
                    onClick = { nav.navigate(ChatRoutes.detail("chat_${contact.id}")) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    ListItem(
                        headlineContent = { Text(contact.name) },
                        supportingContent = { Text(contact.emailOrPhone ?: "") }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ContactsPane(
    nav: NavController,
    vm: ContactsViewModel,
    query: String = ""
) {
    val state by vm.state.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val cardShape = MaterialTheme.shapes.medium


    val filtered = remember(state.contacts, query) {
        val q = query.trim().lowercase()
        if (q.isEmpty()) state.contacts
        else state.contacts.filter { c ->
            c.name.lowercase().contains(q) ||
                    (c.emailOrPhone?.lowercase()?.contains(q) == true)
        }
    }

    if (query.isNotBlank()) {
        if (state.isLoading) LinearProgressIndicator(Modifier.fillMaxWidth())

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filtered, key = { it.id }) { contact ->
                val scope = rememberCoroutineScope()
                val density = LocalDensity.current
                val revealWidth = 88.dp
                val revealPx = with(density) { revealWidth.toPx() }
                val offsetX = remember { Animatable(0f) }     // 0f = closed, -revealPx = open
                var showConfirm by rememberSaveable { mutableStateOf(false) }

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
                                    val target =
                                        if (offsetX.value <= -revealPx / 2f) -revealPx else 0f
                                    scope.launch { offsetX.animateTo(target, tween(180)) }
                                },
                                onDragCancel = {
                                    scope.launch { offsetX.animateTo(0f, tween(180)) }
                                }
                            )
                        }
                ) {
                    // Orange delete tray (under the card)
                    Row(
                        modifier = Modifier
                            .matchParentSize()
                            .padding(vertical = 6.dp)      // match card’s vertical padding
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

                    // Foreground card slides horizontally
                    ElevatedCard(
                        onClick = {
                            scope.launch { offsetX.animateTo(0f, tween(160)) }
                            nav.navigate(ChatRoutes.detail("chat_${contact.id}"))
                        },
                        shape = cardShape,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                    ) {
                        ListItem(
                            leadingContent = {
                                Avatar(
                                    name = contact.name,
                                    url = contact.avatarUrl
                                )
                            },
                            headlineContent = { Text(contact.name) },
                            supportingContent = {
                                val detail = remember(contact.emailOrPhone) {
                                    contact.emailOrPhone?.takeIf { it.isNotBlank() } ?: "No contact info"
                                }
                                Text(
                                    text = detail,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                            }

                        )
                    }
                }

                // Per-row confirm dialog
                if (showConfirm) {
                    AlertDialog(
                        onDismissRequest = {
                            showConfirm = false
                            scope.launch { offsetX.animateTo(0f, tween(160)) }
                        },
                        title = { Text("Delete contact?") },
                        text = { Text("This will remove the contact and its 1:1 chat/call logs. This can’t be undone.") },
                        confirmButton = {
                            TextButton(onClick = {
                                showConfirm = false
                                vm.deleteContact(contact.id)   // cascades remove chat & calls
                            }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                showConfirm = false
                                scope.launch { offsetX.animateTo(0f, tween(160)) }
                            }) { Text("Cancel") }
                        }
                    )
                }
            }

        }
        return
    }

    data class Section(val key: String, val header: String, val items: List<Contact>)

    val favorites = filtered.filter { it.isStarred }
    val others = filtered.filter { !it.isStarred }

    val letterGroups: Map<Char, List<Contact>> = others.groupBy { c ->
        c.name.firstOrNull()?.uppercaseChar()?.takeIf { it in 'A'..'Z' } ?: '#'
    }.mapValues { (_, list) -> list.sortedBy { it.name } }

    val sections = buildList {
        if (favorites.isNotEmpty()) add(Section(key = "*", header = "Favorites", items = favorites))
        ('A'..'Z').forEach { ch ->
            val bucket = letterGroups[ch]
            if (!bucket.isNullOrEmpty()) add(Section(key = ch.toString(), header = ch.toString(), items = bucket))
        }
        val hash = letterGroups['#']
        if (!hash.isNullOrEmpty()) add(Section(key = "#", header = "#", items = hash))
    }

    val headerIndex: Map<String, Int> = remember(sections) {
        var idx = 0
        idx += 2
        buildMap {
            sections.forEach { sec ->
                put(sec.key, idx)
                idx += 1 + sec.items.size
            }
        }
    }

    if (state.isLoading) LinearProgressIndicator(Modifier.fillMaxWidth())

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                ElevatedCard(
                    onClick = { nav.navigate(ChatRoutes.NEW_CONTACT) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    ListItem(
                        leadingContent = {
                            Icon(Icons.Filled.PersonAdd, contentDescription = null, tint = Color(0xFF1DB954))
                        },
                        headlineContent = { Text("New contact") },
                        supportingContent = { Text("Start a new chat") }
                    )
                }
            }
            item {
                ElevatedCard(
                    onClick = { nav.navigate(ChatRoutes.GROUP_CREATE) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    ListItem(
                        leadingContent = {
                            Icon(Icons.Filled.Group, contentDescription = null, tint = Color(0xFF1DB954))
                        },
                        headlineContent = { Text("New group") },
                        supportingContent = { Text("Create a group conversation") }
                    )
                }
            }

            sections.forEach { section ->
                stickyHeader {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (section.key == "*") {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFA000),
                                modifier = Modifier.padding(end = 6.dp)
                            )
                            Text("Favorites", style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        } else {
                            Text(section.header, style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Divider()
                }
                items(section.items, key = { it.id }) { contact ->
                    val scope = rememberCoroutineScope()
                    val density = LocalDensity.current
                    val revealWidth = 88.dp
                    val revealPx = with(density) { revealWidth.toPx() }
                    val offsetX = remember { Animatable(0f) }     // 0f = closed, -revealPx = open
                    var showConfirm by rememberSaveable { mutableStateOf(false) }

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
                        // Orange delete tray (under the card)
                        Row(
                            modifier = Modifier
                                .matchParentSize()
                                .padding(vertical = 6.dp)
                                .clip(cardShape)
                                .background(Color(0xFFFF7A00)),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(Modifier.weight(1f))
                            IconButton(
                                onClick = { showConfirm = true },
                                modifier = Modifier.padding(end = 16.dp)
                            ) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.White)
                            }
                        }

                        // Foreground card slides horizontally
                        ElevatedCard(
                            onClick = {
                                scope.launch { offsetX.animateTo(0f, tween(160)) }
                                nav.navigate(ChatRoutes.detail("chat_${contact.id}"))
                            },
                            shape = cardShape,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                        ) {
                            ListItem(
                                leadingContent = { Avatar(name = contact.name, url = contact.avatarUrl) },
                                headlineContent = { Text(contact.name) },
                                // under headlineContent = { Text(contact.name) },
                                supportingContent = {
                                    val detail = remember(contact.emailOrPhone) {
                                        contact.emailOrPhone?.takeIf { it.isNotBlank() } ?: "No contact info"
                                    }
                                    Text(
                                        text = detail,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1
                                    )
                                }

                            )
                        }
                    }

                    // Per-row confirm dialog
                    if (showConfirm) {
                        AlertDialog(
                            onDismissRequest = {
                                showConfirm = false
                                scope.launch { offsetX.animateTo(0f, tween(160)) }
                            },
                            title = { Text("Delete contact?") },
                            text = { Text("This will remove the contact and its 1:1 chat/call logs. This can’t be undone.") },
                            confirmButton = {
                                TextButton(onClick = {
                                    showConfirm = false
                                    vm.deleteContact(contact.id)
                                }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
                            },
                            dismissButton = {
                                TextButton(onClick = {
                                    showConfirm = false
                                    scope.launch { offsetX.animateTo(0f, tween(160)) }
                                }) { Text("Cancel") }
                            }
                        )
                    }
                }


            }
        }

        val alphabet = ('A'..'Z').map { it.toString() }
        val showStar = favorites.isNotEmpty()

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showStar) {
                val enabled = headerIndex.containsKey("*")
                Text(
                    text = "★",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (enabled) 1f else 0.35f),
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            headerIndex["*"]?.let { target ->
                                scope.launch { listState.animateScrollToItem(target) }
                            }
                        }
                )
            }

            alphabet.forEach { letter ->
                val enabled = headerIndex.containsKey(letter)
                Text(
                    text = letter,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (enabled) 1f else 0.35f),
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            val targetIndex = headerIndex[letter] ?: run {
                                val next = alphabet
                                    .dropWhile { it[0] < letter[0] }
                                    .firstOrNull { headerIndex.containsKey(it) }
                                val prev = alphabet
                                    .takeWhile { it[0] <= letter[0] }
                                    .lastOrNull { headerIndex.containsKey(it) }
                                headerIndex[next] ?: headerIndex[prev]
                            }
                            targetIndex?.let { scope.launch { listState.animateScrollToItem(it) } }
                        }
                )
            }
        }
    }
}
