package com.example.chat.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Badge
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.chat.domain.model.ChatSummary

@Composable
fun ChatListItem(chat: ChatSummary, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        ListItem(
            leadingContent = { Avatar(name = chat.title, url = chat.avatarUrl) }, // ← shared Avatar
            headlineContent = { Text(chat.title, fontWeight = FontWeight.SemiBold) },
            supportingContent = { Text(chat.lastMessage) },
            trailingContent = {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        friendlyDate(chat.lastTimestamp),
                        style = MaterialTheme.typography.labelSmall
                    )
                    if (chat.unreadCount > 0) {
                        Spacer(Modifier.height(6.dp))
                        Badge { Text(chat.unreadCount.toString()) }
                    }
                }
            }
        )
    }
}
