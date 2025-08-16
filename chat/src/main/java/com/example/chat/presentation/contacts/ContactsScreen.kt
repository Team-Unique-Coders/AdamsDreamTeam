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

@Composable
fun ContactsPane(nav: NavController, vm: ContactsViewModel) {
    val state by vm.state.collectAsState()

    if (state.isLoading) {
        LinearProgressIndicator(Modifier.fillMaxWidth())
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ) {
        // NEW: quick actions at the top
        item {
            ElevatedCard(
                onClick = { nav.navigate(ChatRoutes.GROUP_CREATE) },
                        // dummy group chat
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                ListItem(
                    leadingContent = { Icon(Icons.Filled.Group, contentDescription = null) },
                    headlineContent = { Text("New group") },
                    supportingContent = { Text("Create a group conversation") }
                )
            }
        }
        item {
            ElevatedCard(
                onClick = { nav.navigate(ChatRoutes.NEW_CONTACT) }, // dummy new contact flow
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                ListItem(
                    leadingContent = { Icon(Icons.Filled.PersonAdd, contentDescription = null) },
                    headlineContent = { Text("New contact") },
                    supportingContent = { Text("Start a new chat") }
                )
            }
        }

        // Existing contacts
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
