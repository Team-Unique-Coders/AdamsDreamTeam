package com.example.chat.presentation.contacts

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

