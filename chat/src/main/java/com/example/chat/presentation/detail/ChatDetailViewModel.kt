package com.example.chat.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.chat.domain.model.Message
import com.example.chat.domain.repository.ChatRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatDetailUiState(
    val chatId: String,
    val messages: List<Message> = emptyList(),
    val input: String = "",
    val isSending: Boolean = false,
    val isPeerTyping: Boolean = false,
    val title: String = "Conversation"
)

class ChatDetailViewModel(
    private val repo: ChatRepository,
    savedState: SavedStateHandle
) : ViewModel() {

    private val chatId: String = checkNotNull(savedState["chatId"])

    private val _state = MutableStateFlow(ChatDetailUiState(chatId))
    val state: StateFlow<ChatDetailUiState> = _state.asStateFlow()

    init {
        // messages stream
        viewModelScope.launch {
            repo.messages(chatId).collect { msgs ->
                _state.update { it.copy(messages = msgs) }
            }
        }
        // title (person/group name) from chats list
        viewModelScope.launch {
            repo.chats().collect { list ->
                val name = list.firstOrNull { it.id == chatId }?.title ?: "Conversation"
                _state.update { it.copy(title = name) }
            }
        }
    }

    fun onInputChange(value: String) = _state.update { it.copy(input = value) }

    fun send() {
        val text = state.value.input.trim()
        if (text.isEmpty()) return
        _state.update { it.copy(isSending = true) }
        viewModelScope.launch {
            repo.     sendMessage(chatId, fromUserId = "me", text = text)
            _state.update { it.copy(input = "", isSending = false) }
            simulatePeerReply()
        }
    }

    private fun simulatePeerReply() {
        viewModelScope.launch {
            _state.update { it.copy(isPeerTyping = true) }
            delay(900)
            _state.update { it.copy(isPeerTyping = false) }
            repo.sendMessage(chatId, fromUserId = "bot", text = "Got it! 👍")
        }
    }

    companion object {
        fun factory(repo: ChatRepository) = viewModelFactory {
            initializer { ChatDetailViewModel(repo, this.createSavedStateHandle()) }
        }
    }
}
