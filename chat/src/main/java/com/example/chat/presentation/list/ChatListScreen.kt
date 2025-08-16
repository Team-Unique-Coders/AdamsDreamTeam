package com.example.chat.presentation.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chat.navigation.ChatRoutes
import com.example.chat.presentation.ui.ChatListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(nav: NavController, vm: ChatListViewModel) {
    val state by vm.state.collectAsState()
    var query by remember { mutableStateOf("") } // search placeholder; not filtering yet

    Scaffold(
        topBar = { TopAppBar(title = { Text("Chat") }) }
    ) { padding ->
        Column(Modifier.padding(padding)) {

            // Tabs like your design
            TabRow(selectedTabIndex = 0) {
                Tab(selected = true, onClick = {}) { Text("Chats", Modifier.padding(16.dp)) }
                Tab(selected = false, onClick = { nav.navigate(ChatRoutes.CONTACTS) }) {
                    Text("Contacts", Modifier.padding(16.dp))
                }
                Tab(selected = false, onClick = {}) { Text("Calls", Modifier.padding(16.dp)) }
            }

            // Optional: a simple search field (no logic yet)
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
fun ChatListPane(nav: NavController, vm: ChatListViewModel) {
    val state by vm.state.collectAsState()
    var query by remember { mutableStateOf("") }

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

