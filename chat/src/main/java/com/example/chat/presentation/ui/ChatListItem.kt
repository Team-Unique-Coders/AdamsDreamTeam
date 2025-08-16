package com.example.chat.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
            leadingContent = { ChatAvatar(name = chat.title) },   // initials in a circle
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

@Composable
private fun ChatAvatar(name: String) {
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
        Text(initials, style = MaterialTheme.typography.labelLarge)
    }
}
