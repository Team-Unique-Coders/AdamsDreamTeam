package com.example.chat.data.fake

import com.example.chat.domain.model.ChatSummary
import com.example.chat.domain.model.Contact
import com.example.chat.domain.model.Message
import com.example.chat.domain.model.MessageType
import com.example.chat.domain.repository.ChatRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import java.util.UUID

class FakeChatRepository : ChatRepository {

    private val meId = "me"

    // ---- Contacts (static) ----
    private val contacts = listOf(
        Contact("c1", "Rebecca Moore", emailOrPhone = "rebeccaca@gmail.com", isStarred = true),
        Contact("c2", "Franz Ferdinand", emailOrPhone = "Ferdinandand@gmail.com"),
        Contact("c3", "My buddyz", emailOrPhone = "0678564789"),
        Contact("c4", "Burger delivery", emailOrPhone = "support@burger.com")
    )

    // ---- Chats list (use canonical ids: chat_<contactId>) ----
    private val chatsState = MutableStateFlow(
        listOf(
            ChatSummary(
                id = "chat_c1",
                title = "Rebecca Moore",
                avatarUrl = null,
                lastMessage = "Hi my love! How are you today? 😍",
                lastTimestamp = System.currentTimeMillis()
            ),
            ChatSummary(
                id = "chat_c2",
                title = "Franz Ferdinand",
                avatarUrl = null,
                lastMessage = "Check it out!",
                lastTimestamp = System.currentTimeMillis() - 86_400_000L
            ),
            ChatSummary(
                id = "chat_c3",
                title = "My buddyz",
                avatarUrl = null,
                lastMessage = "So what??? Please tell me why you can’t …",
                lastTimestamp = System.currentTimeMillis() - 2 * 86_400_000L,
                unreadCount = 2
            ),
            ChatSummary(
                id = "chat_c4",
                title = "Burger delivery",
                avatarUrl = null,
                lastMessage = "Instant delivery !",
                lastTimestamp = System.currentTimeMillis() - 3 * 86_400_000L
            )
        )
    )

    // ---- Messages per chat (keys match chat_<contactId>) ----
    // ---- Messages per chat (keys match chat_<contactId>) ----
    private val messagesByChat: MutableMap<String, MutableStateFlow<List<Message>>> =
        mutableMapOf<String, MutableStateFlow<List<Message>>>().apply {
            put("chat_c1", MutableStateFlow(seedFor("chat_c1", "c1")))
            put("chat_c2", MutableStateFlow(seedFor("chat_c2", "c2")))
            put("chat_c3", MutableStateFlow(seedFor("chat_c3", "c3")))
            put("chat_c4", MutableStateFlow(seedFor("chat_c4", "c4")))
        }

    // Replace the old `seed(...)` with this version that varies by chat
    private fun seedFor(chatId: String, otherId: String): List<Message> {
        val now = System.currentTimeMillis()
        fun msg(from: String, secondsAgo: Long, text: String) = Message(
            id = UUID.randomUUID().toString(),
            chatId = chatId,
            senderId = from,
            sentAt = now - secondsAgo * 1000,
            type = MessageType.TEXT,
            text = text
        )

        return when (chatId) {
            "chat_c1" -> listOf(
                msg(otherId, 300, "Hey 👋"),
                msg(meId,    240, "Hello!"),
                msg(otherId,  10, "Hi my love! How are you today? 😍") // last bubble (Rebecca)
            )
            "chat_c2" -> listOf(
                msg(otherId, 200, "Yo"),
                msg(meId,    180, "Sup"),
                msg(otherId,  20, "Check it out!")                     // last bubble (Franz)
            )
            "chat_c3" -> listOf(
                msg(otherId, 400, "Bro??"),
                msg(meId,    380, "What"),
                msg(otherId,  30, "So what??? Please tell me why you can’t …") // last (Buddyz)
            )
            "chat_c4" -> listOf(
                msg(otherId, 500, "Order #A1278"),
                msg(meId,    480, "On the way?"),
                msg(otherId,  40, "Instant delivery !")                // last (Burger)
            )
            else -> listOf(
                msg(otherId, 120, "Hi!"),
                msg(meId,     60, "Hello 👋")
            )
        }
    }


    // Lazily create a thread when opening from Contacts if it doesn't exist
    private fun ensureThread(chatId: String) {
        if (!messagesByChat.containsKey(chatId)) {
            val flow = MutableStateFlow<List<Message>>(emptyList())
            messagesByChat[chatId] = flow

            val contactId = chatId.removePrefix("chat_")
            val contact = contacts.firstOrNull { it.id == contactId }
            val name = contact?.name ?: "New Chat"

            val now = System.currentTimeMillis()
            val greeting = Message(
                id = UUID.randomUUID().toString(),
                chatId = chatId,
                senderId = contactId,   // message from the contact
                sentAt = now - 30_000L,
                type = MessageType.TEXT,
                text = "Hi there 👋"
            )
            flow.value = listOf(greeting)

            if (chatsState.value.none { it.id == chatId }) {
                chatsState.value = chatsState.value + ChatSummary(
                    id = chatId,
                    title = name,
                    avatarUrl = null,
                    lastMessage = greeting.text ?: "",
                    lastTimestamp = greeting.sentAt
                )
            }
        }
    }

    // ---- Contract implementation ----
    override fun chats(): StateFlow<List<ChatSummary>> = chatsState

    override fun messages(chatId: String) =
        messagesByChat.getOrPut(chatId) {
            ensureThread(chatId)           // ensure existing or create-once
            messagesByChat[chatId]!!       // safe after ensureThread
        }

    override fun contacts() = flowOf(contacts)

    override suspend fun sendMessage(chatId: String, fromUserId: String, text: String) {
        delay(200)
        val flow = messagesByChat.getOrPut(chatId) { MutableStateFlow(emptyList()) }
        flow.value = flow.value + Message(
            id = UUID.randomUUID().toString(),
            chatId = chatId,
            senderId = fromUserId,
            sentAt = System.currentTimeMillis(),
            type = MessageType.TEXT,
            text = text
        )

        val now = System.currentTimeMillis()
        val exists = chatsState.value.any { it.id == chatId }
        chatsState.value = if (!exists) {
            val name = contacts.find { "chat_${it.id}" == chatId }?.name ?: "New Chat"
            chatsState.value + ChatSummary(
                id = chatId,
                title = name,
                avatarUrl = null,
                lastMessage = text,
                lastTimestamp = now
            )
        } else {
            chatsState.value.map {
                if (it.id == chatId) it.copy(lastMessage = text, lastTimestamp = now) else it
            }
        }
    }
}
