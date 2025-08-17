package com.example.chat.domain.repository

import com.example.chat.domain.model.CallLog
import com.example.chat.domain.model.ChatSummary
import com.example.chat.domain.model.Contact
import com.example.chat.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun chats(): Flow<List<ChatSummary>>
    fun messages(chatId: String): Flow<List<Message>>
    fun contacts(): Flow<List<Contact>>

    fun calls(): Flow<List<CallLog>>
    suspend fun sendMessage(chatId: String, fromUserId: String, text: String)

    suspend fun createGroupChat(title: String, memberIds: List<String>): String

    suspend fun createContact(name: String, emailOrPhone: String?): String

    suspend fun markRead(chatId: String)


    suspend fun deleteChat(chatId: String)
    suspend fun deleteContact(contactId: String)
    suspend fun deleteCall(callId: String)



}
