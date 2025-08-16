package com.example.chat.presentation.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.chat.domain.model.Contact
import com.example.chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.*

data class ContactsUiState(
    val contacts: List<Contact> = emptyList(),
    val isLoading: Boolean = true
)

class ContactsViewModel(private val repo: ChatRepository) : ViewModel() {
    val state: StateFlow<ContactsUiState> =
        repo.contacts()
            .map { ContactsUiState(it, isLoading = false) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ContactsUiState())

    companion object {
        fun factory(repo: ChatRepository) = viewModelFactory {
            initializer { ContactsViewModel(repo) }
        }
    }
}
