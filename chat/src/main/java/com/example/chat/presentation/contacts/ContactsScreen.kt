package com.example.chat.presentation.contacts

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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.chat.domain.model.Contact
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import coil.compose.SubcomposeAsyncImage
import androidx.compose.ui.layout.ContentScale





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
    query: String = ""              // ← from top-bar search
) {
    val state by vm.state.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // ----- filter -----
    val filtered = remember(state.contacts, query) {
        val q = query.trim().lowercase()
        if (q.isEmpty()) state.contacts
        else state.contacts.filter { c ->
            c.name.lowercase().contains(q) ||
                    (c.emailOrPhone?.lowercase()?.contains(q) == true)
        }
    }

    // When searching: flat list (no quick actions, no sections, no index bar)
    if (query.isNotBlank()) {
        if (state.isLoading) LinearProgressIndicator(Modifier.fillMaxWidth())

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filtered) { contact ->
                ElevatedCard(
                    onClick = { nav.navigate(ChatRoutes.detail("chat_${contact.id}")) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ListItem(
                        leadingContent = { ContactAvatar(name = contact.name, url = contact.avatarUrl) },
                        headlineContent = { Text(contact.name) },
                        supportingContent = { Text(contact.emailOrPhone ?: "") }
                    )
                }
            }
        }
        return
    }

    // ----- build sections (Favorites + A..Z) -----
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

    // Pre-compute header indices for fast scroll (include quick actions items)
    val headerIndex: Map<String, Int> = remember(sections) {
        var idx = 0
        idx += 2 // quick actions (New contact, New group)
        buildMap {
            sections.forEach { sec ->
                put(sec.key, idx)
                idx += 1 + sec.items.size // header + rows
            }
        }
    }

    // ----- UI -----
    if (state.isLoading) LinearProgressIndicator(Modifier.fillMaxWidth())

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(16.dp)
        ) {
            // Quick actions
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

            // Sectioned contacts
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
                items(section.items) { contact ->
                    ElevatedCard(
                        onClick = { nav.navigate(ChatRoutes.detail("chat_${contact.id}")) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        ListItem(
                            leadingContent = { ContactAvatar(name = contact.name, url = contact.avatarUrl) },
                            headlineContent = { Text(contact.name) },
                            supportingContent = { Text(contact.emailOrPhone ?: "") }
                        )

                    }
                }
            }
        }

        // Right-side index bar (hidden if no sections)
        // Right-side index bar: always show A–Z; optional ★ if favorites exist
        val alphabet = ('A'..'Z').map { it.toString() }
        val showStar = favorites.isNotEmpty()

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Optional ★ for Favorites
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

            // A–Z always visible. If a letter has no section, dim it and jump to nearest available section.
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

@Composable
private fun ContactAvatar(name: String, url: String?) {
    val initials = remember(name) {
        name.split(" ")
            .mapNotNull { it.firstOrNull()?.uppercaseChar()?.toString() }
            .take(2)
            .joinToString("")
    }
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        if (url.isNullOrBlank()) {
            // Fallback: initials
            Text(initials, style = MaterialTheme.typography.labelLarge)
        } else {
            // Load photo; while loading or on error, show initials
            SubcomposeAsyncImage(
                model = url,
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                loading = { Text(initials, style = MaterialTheme.typography.labelLarge) },
                error   = { Text(initials, style = MaterialTheme.typography.labelLarge) }
            )
        }
    }
}
