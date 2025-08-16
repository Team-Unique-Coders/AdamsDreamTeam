package com.example.chat.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chat.domain.model.ChatSummary

@Composable
fun ChatListItem(
    chat: ChatSummary,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        ListItem(
            leadingContent = {
                // Placeholder avatar; swap with Coil later
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            },
            headlineContent = { Text(chat.title, style = MaterialTheme.typography.titleMedium) },
            supportingContent = { Text(chat.lastMessage, maxLines = 1) },
            trailingContent = {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = friendlyDate(chat.lastTimestamp),
                        style = MaterialTheme.typography.labelSmall
                    )
                    if (chat.unreadCount > 0) {
                        Spacer(Modifier.height(6.dp))
                        Badge { Text("${chat.unreadCount}") }
                    }
                }
            },
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        )
    }
}

