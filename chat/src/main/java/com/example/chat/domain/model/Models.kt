package com.example.chat.domain.model

enum class MessageType { TEXT, IMAGE, AUDIO }
enum class MessageStatus { SENT, DELIVERED, READ }

data class Contact(
    val id: String,
    val name: String,
    val avatarUrl: String? = null,
    val emailOrPhone: String? = null,
    val isStarred: Boolean = false
)

data class Message(
    val id: String,
    val chatId: String,
    val senderId: String,
    val sentAt: Long,
    val type: MessageType = MessageType.TEXT,
    val text: String? = null,
    val mediaUrl: String? = null,
    val durationSec: Int? = null,
    val status: MessageStatus = MessageStatus.SENT
)

data class ChatSummary(
    val id: String,
    val title: String,
    val avatarUrl: String?,
    val lastMessage: String,
    val lastTimestamp: Long,
    val unreadCount: Int = 0
)
