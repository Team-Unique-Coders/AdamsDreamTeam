package com.example.chat.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.Mood
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(nav: NavController, vm: ChatDetailViewModel) {
    val state by vm.state.collectAsState()

    // Auto-scroll to bottom when messages change
    val listState = rememberLazyListState()
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.lastIndex)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.title) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Video call
                    IconButton(onClick = { /* TODO: start video call (stub) */ }) {
                        Icon(Icons.Filled.Videocam, contentDescription = "Video call")
                    }
                    // Voice call
                    IconButton(onClick = { /* TODO: start voice call (stub) */ }) {
                        Icon(Icons.Filled.Call, contentDescription = "Call")
                    }
                    // Overflow/menu
                    IconButton(onClick = { /* TODO: menu */ }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Messages
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.messages) { m ->
                    val isMe = m.senderId == "me"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                    ) {
                        Surface(
                            color = if (isMe) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.large,
                            tonalElevation = if (isMe) 2.dp else 0.dp
                        ) {
                            Text(
                                m.text.orEmpty(),
                                modifier = Modifier.padding(12.dp),
                                color = if (isMe) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // "Typing…" indicator
            if (state.isPeerTyping) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Typing…",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Divider()

            // Composer bar
            // Composer bar
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Text field with emoji (left) + attach & camera (right) inside the field
                OutlinedTextField(
                    value = state.input,
                    onValueChange = vm::onInputChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    placeholder = { Text("Message") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = {
                        if (state.input.isNotBlank()) vm.send()
                    }),
                    leadingIcon = {
                        IconButton(onClick = { /* TODO: emoji picker */ }) {
                            Icon(Icons.Outlined.Mood, contentDescription = "Emoji")
                        }
                    },
                    trailingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { /* TODO: attach */ }) {
                                Icon(Icons.Outlined.AttachFile, contentDescription = "Attach")
                            }
                            IconButton(onClick = { /* TODO: camera */ }) {
                                Icon(Icons.Outlined.PhotoCamera, contentDescription = "Camera")
                            }
                        }
                    }
                )

                // Right action: Mic when empty, Send when typing
                val canSend = state.input.isNotBlank() && !state.isSending
                if (canSend) {
                    FilledIconButton(onClick = vm::send, enabled = true) {
                        Icon(Icons.Outlined.Send, contentDescription = "Send")
                    }
                } else {
                    FilledIconButton(
                        onClick = {
                            // Stub: send a fake voice message for now
                            vm.onInputChange("🎤 Voice message")
                            vm.send()
                        },
                        enabled = !state.isSending
                    ) {
                        Icon(Icons.Outlined.Mic, contentDescription = "Record")
                    }
                }
            }
        }
    }
}
