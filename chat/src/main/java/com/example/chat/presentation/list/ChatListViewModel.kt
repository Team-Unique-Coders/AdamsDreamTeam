package com.example.chat.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.chat.domain.model.ChatSummary
import com.example.chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChatListUiState(
    val chats: List<ChatSummary> = emptyList(),
    val isLoading: Boolean = true
)

class ChatListViewModel(private val repo: ChatRepository) : ViewModel() {

    // Single source of truth: newest-first ordering
    val state: StateFlow<ChatListUiState> = repo.chats()
        .map { list ->
            ChatListUiState(
                chats = list.sortedByDescending { it.lastTimestamp },
                isLoading = false
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            ChatListUiState(isLoading = true)
        )

    fun deleteChat(chatId: String) {
        viewModelScope.launch { repo.deleteChat(chatId) }
    }

    companion object {
        fun factory(repo: ChatRepository) = viewModelFactory {
            initializer { ChatListViewModel(repo) }
        }
    }
}
