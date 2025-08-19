package com.example.chat.data.fake

import com.example.chat.domain.model.CallLog
import com.example.chat.domain.model.CallType
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
    private val contactsState = MutableStateFlow(
        listOf(
            Contact(
                id = "c1",
                name = "Rebecca Moore",
                avatarUrl = null,
                emailOrPhone = "+1 415-555-0137",
                isStarred = true
            ),
            Contact(
                id = "c2",
                name = "Franz Ferdinand",
                avatarUrl = null,
                emailOrPhone = "franz.ferdinand@example.com"
            ),
            Contact(
                id = "c3",
                name = "My buddyz",
                avatarUrl = null,
                emailOrPhone = "+44 7700 900123"
            ),
            Contact(
                id = "c4",
                name = "Burger delivery",
                avatarUrl = null,
                emailOrPhone = "support@burger.com"
            ),
            // a couple more so A–Z & favorites look good
            Contact(
                id = "c5",
                name = "Amy Adams",
                avatarUrl = null,
                emailOrPhone = "amy@adams.co",
                isStarred = true
            ),
            Contact(
                id = "c6",
                name = "Zoe Zhang",
                avatarUrl = null,
                emailOrPhone = "+65 8123 4567"
            ),
            Contact(
                id = "c7",
                name = "Carlos Mendez",
                avatarUrl = null,
                emailOrPhone = "carlos.m@example.com"
            )
        )
    )



    private val chatsState = MutableStateFlow(
        listOf(
            ChatSummary(id = "chat_c1", title = "Rebecca Moore",  avatarUrl = null,
                lastMessage = "Hi my love! How are you today? 😍", lastTimestamp = System.currentTimeMillis()),
            ChatSummary(id = "chat_c2", title = "Franz Ferdinand", avatarUrl = null,
                lastMessage = "Check it out!", lastTimestamp = System.currentTimeMillis() - 86_400_000L),
            ChatSummary(id = "chat_c3", title = "My buddyz",       avatarUrl = null,
                lastMessage = "So what??? Please tell me why you can’t …",
                lastTimestamp = System.currentTimeMillis() - 2 * 86_400_000L, unreadCount = 2),
            ChatSummary(id = "chat_c4", title = "Burger delivery", avatarUrl = null,
                lastMessage = "Instant delivery !", lastTimestamp = System.currentTimeMillis() - 3 * 86_400_000L)
        )
    )

    private val messagesByChat: MutableMap<String, MutableStateFlow<List<Message>>> =
        mutableMapOf<String, MutableStateFlow<List<Message>>>().apply {
            put("chat_c1", MutableStateFlow(seedFor("chat_c1", "c1")))
            put("chat_c2", MutableStateFlow(seedFor("chat_c2", "c2")))
            put("chat_c3", MutableStateFlow(seedFor("chat_c3", "c3")))
            put("chat_c4", MutableStateFlow(seedFor("chat_c4", "c4")))
        }

    init {
        syncAllSummariesFromMessages()
    }

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
                msg(otherId,  10, "Hi my love! How are you today? 😍")
            )
            "chat_c2" -> listOf(
                msg(otherId, 200, "Yo"),
                msg(meId,    180, "Sup"),
                msg(otherId,  20, "Check it out!")
            )
            "chat_c3" -> listOf(
                msg(otherId, 400, "Bro??"),
                msg(meId,    380, "What"),
                msg(otherId,  30, "So what??? Please tell me why you can’t come?")
            )
            "chat_c4" -> listOf(
                msg(otherId, 500, "Order #A1278"),
                msg(meId,    480, "On the way?"),
                msg(otherId,  40, "Instant delivery !")
            )
            else -> listOf(
                msg(otherId, 120, "Hi!"),
                msg(meId,     60, "Hello 👋")
            )
        }
    }

    private val callsState = MutableStateFlow(
        listOf(
            // Today
            CallLog("cl1",  "c1", System.currentTimeMillis() - 15 * 60_000L,      180, CallType.OUTGOING, isVideo = true),
            CallLog("cl2",  "c2", System.currentTimeMillis() - 45 * 60_000L,       95, CallType.INCOMING, isVideo = false),
            CallLog("cl3",  "c3", System.currentTimeMillis() - 80 * 60_000L,        0, CallType.MISSED,   isVideo = true),
            CallLog("cl4",  "c4", System.currentTimeMillis() - 3  * 60 * 60_000L,  300, CallType.INCOMING, isVideo = false),

            // Yesterday
            CallLog("cl5",  "c1", System.currentTimeMillis() - 26 * 60 * 60_000L,   60, CallType.OUTGOING, isVideo = true),
            CallLog("cl6",  "c2", System.currentTimeMillis() - 30 * 60 * 60_000L,    0, CallType.MISSED,   isVideo = false),

            // Older
            CallLog("cl7",  "c3", System.currentTimeMillis() - 53 * 60 * 60_000L,  125, CallType.INCOMING, isVideo = false),
            CallLog("cl8",  "c4", System.currentTimeMillis() - 72 * 60 * 60_000L,  240, CallType.OUTGOING, isVideo = true),
            CallLog("cl9",  "c2", System.currentTimeMillis() - 5  * 24 * 60 * 60_000L, 0,  CallType.MISSED,   isVideo = false),
            CallLog("cl10", "c1", System.currentTimeMillis() - 7  * 24 * 60 * 60_000L, 30, CallType.INCOMING, isVideo = true)
        )
    )



    private fun ensureThread(chatId: String) {
        if (!messagesByChat.containsKey(chatId)) {
            val flow = MutableStateFlow<List<Message>>(emptyList())
            messagesByChat[chatId] = flow

            val now = System.currentTimeMillis()

            if (chatId.startsWith("chat_")) {
                val contactId = chatId.removePrefix("chat_")
                val contact = contactsState.value.firstOrNull { it.id == contactId }
                val name = contact?.name ?: "New Chat"
                val greeting = Message(
                    id = UUID.randomUUID().toString(),
                    chatId = chatId,
                    senderId = contactId,
                    sentAt = now - 30_000L,
                    type = MessageType.TEXT,
                    text = "Hi there 👋"
                )
                flow.value = listOf(greeting)

                if (chatsState.value.none { it.id == chatId }) {
                    chatsState.value = chatsState.value + ChatSummary(
                        id = chatId, title = name, avatarUrl = null,
                        lastMessage = greeting.text ?: "", lastTimestamp = greeting.sentAt
                    )
                }
            } else if (chatId.startsWith("group_")) {
                // For groups created without messages yet
                if (chatsState.value.none { it.id == chatId }) {
                    chatsState.value = chatsState.value + ChatSummary(
                        id = chatId, title = "New Group", avatarUrl = null,
                        lastMessage = "Group created", lastTimestamp = now
                    )
                }
                val sys = Message(
                    id = UUID.randomUUID().toString(),
                    chatId = chatId,
                    senderId = "system",
                    sentAt = now - 10_000L,
                    type = MessageType.TEXT,
                    text = "Group created"
                )
                flow.value = listOf(sys)
            }

            syncSummaryFromMessages(chatId)
        }
    }

        private fun syncSummaryFromMessages(chatId: String) {
        val last = messagesByChat[chatId]?.value?.lastOrNull() ?: return
        chatsState.value = chatsState.value.map {
            if (it.id == chatId) it.copy(lastMessage = last.text ?: "", lastTimestamp = last.sentAt)
            else it
        }
    }
    private fun syncAllSummariesFromMessages() {
        messagesByChat.keys.forEach { syncSummaryFromMessages(it) }
    }

    override fun chats(): StateFlow<List<ChatSummary>> = chatsState

    override fun messages(chatId: String) =
        messagesByChat.getOrPut(chatId) {
            ensureThread(chatId)
            messagesByChat[chatId]!!
        }

    override fun contacts() = contactsState

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

        if (chatsState.value.none { it.id == chatId }) {
            val name =
                if (chatId.startsWith("chat_")) {
                    val cid = chatId.removePrefix("chat_")
                    contactsState.value.find { it.id == cid }?.name ?: "New Chat"
                } else if (chatId.startsWith("group_")) {
                    "New Group"
                } else "New Chat"

            chatsState.value = chatsState.value + ChatSummary(
                id = chatId, title = name, avatarUrl = null,
                lastMessage = text, lastTimestamp = System.currentTimeMillis()
            )
        } else {
            syncSummaryFromMessages(chatId)
        }
    }

    override suspend fun createGroupChat(title: String, memberIds: List<String>): String {
        val id = "group_${UUID.randomUUID()}"
        val now = System.currentTimeMillis()

        val createdMsg = Message(
            id = UUID.randomUUID().toString(),
            chatId = id,
            senderId = "system",
            sentAt = now,
            type = MessageType.TEXT,
            text = "Group \"$title\" created"
        )
        messagesByChat[id] = MutableStateFlow(listOf(createdMsg))

        chatsState.value = chatsState.value + ChatSummary(
            id = id,
            title = title.ifBlank { "New Group" },
            avatarUrl = null,
            lastMessage = createdMsg.text ?: "",
            lastTimestamp = now
        )

        return id
    }

    override suspend fun markRead(chatId: String) {
        chatsState.value = chatsState.value.map { chat ->
            if (chat.id == chatId) chat.copy(unreadCount = 0) else chat
        }
    }

    override suspend fun createContact(name: String, emailOrPhone: String?): String {
        val id = "c${UUID.randomUUID().toString().take(8)}"
        val contact = Contact(id = id, name = name.ifBlank { "New Contact" }, emailOrPhone = emailOrPhone)
        contactsState.value = contactsState.value + contact
        return id
    }

    override fun calls() = callsState
    override suspend fun deleteChat(chatId: String) {
        chatsState.value = chatsState.value.filterNot { it.id == chatId }
        messagesByChat.remove(chatId)
    }

    override suspend fun deleteContact(contactId: String) {
        contactsState.value = contactsState.value.filterNot { it.id == contactId }

        val possibleChatId = "chat_$contactId"
        if (chatsState.value.any { it.id == possibleChatId }) {
            deleteChat(possibleChatId)
        }

        callsState.value = callsState.value.filterNot { it.contactId == contactId }
    }

    override suspend fun deleteCall(callId: String) {
        callsState.value = callsState.value.filterNot { it.id == callId }
    }

}
