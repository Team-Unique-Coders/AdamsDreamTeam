package com.example.chat.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.chat.domain.model.ChatSummary
import com.example.chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChatListUiState(
    val chats: List<ChatSummary> = emptyList(),
    val isLoading: Boolean = true
)

class ChatListViewModel(private val repo: ChatRepository) : ViewModel() {
    private val _state = MutableStateFlow(ChatListUiState())
    val state: StateFlow<ChatListUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repo.chats()
                .onStart { _state.update { it.copy(isLoading = true) } }
                .collect { list -> _state.value = ChatListUiState(list, isLoading = false) }
        }
    }

    fun deleteChat(chatId: String) {
        viewModelScope.launch {
            repo.deleteChat(chatId)
        }
    }

    companion object {
        fun factory(repo: com.example.chat.domain.repository.ChatRepository) =
            viewModelFactory {
                initializer { ChatListViewModel(repo) }
            }
    }
}
