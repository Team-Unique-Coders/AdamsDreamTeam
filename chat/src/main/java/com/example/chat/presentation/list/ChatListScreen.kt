package com.example.chat.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chat.navigation.ChatRoutes
import com.example.chat.presentation.ui.ChatListItem

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

@Composable
fun ChatListPane(nav: NavController, vm: ChatListViewModel,query: String = "") {
    val state by vm.state.collectAsState()


    val filtered = remember(state.chats, query) {
        val q = query.trim().lowercase()
        if (q.isEmpty()) state.chats
        else state.chats.filter { item ->
            item.title.lowercase().contains(q) ||
                    item.lastMessage.lowercase().contains(q)
        }
    }


    if (state.isLoading) {
        LinearProgressIndicator(Modifier.fillMaxWidth())
    }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filtered, key = { it.id }) { chat ->
            // Dismiss state: delete when swiped End→Start (right-to-left)
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { value ->
                    if (value == SwipeToDismissBoxValue.EndToStart) {
                        vm.deleteChat(chat.id)
                        true
                    } else {
                        false
                    }
                }
            )

            SwipeToDismissBox(
                state = dismissState,
                enableDismissFromStartToEnd = false,   // only allow right-to-left like your mock
                backgroundContent = {
                    // Red background with trash icon, rounded like the card
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.errorContainer),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.weight(1f))
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(end = 24.dp)
                        )
                    }
                }
            ) {
                ChatListItem(chat = chat) {
                    nav.navigate(ChatRoutes.detail(chat.id))
                }
            }

            Spacer(Modifier.height(8.dp))
        }

    }
}

