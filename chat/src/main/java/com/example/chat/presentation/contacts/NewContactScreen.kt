package com.example.chat.presentation.contacts


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.example.chat.domain.repository.ChatRepository
import com.example.chat.navigation.ChatRoutes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ---------- ViewModel ----------

data class NewContactUiState(
    val name: String = "",
    val contact: String = "",
    val isSaving: Boolean = false
)

class NewContactViewModel(private val repo: ChatRepository) : ViewModel() {
    private val _state = MutableStateFlow(NewContactUiState())
    val state: StateFlow<NewContactUiState> = _state.asStateFlow()

    fun onNameChange(v: String) = _state.update { it.copy(name = v) }
    fun onContactChange(v: String) = _state.update { it.copy(contact = v) }

    suspend fun createAndChat(): String {
        _state.update { it.copy(isSaving = true) }
        val id = repo.createContact(
            name = state.value.name,
            emailOrPhone = state.value.contact.ifBlank { null }
        )
        _state.update { it.copy(isSaving = false) }
        return "chat_$id"
    }

    companion object {
        fun factory(repo: ChatRepository) = viewModelFactory {
            initializer { NewContactViewModel(repo) }
        }
    }
}

// ---------- Screen ----------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewContactScreen(nav: NavController, vm: NewContactViewModel) {
    val state by vm.state.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Contact") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (!state.isSaving && state.name.isNotBlank()) {
                        scope.launch {
                            val chatId = vm.createAndChat()
                            nav.navigate(ChatRoutes.detail(chatId)) {
                                popUpTo(ChatRoutes.HOME) { inclusive = false } // keep HOME, drop the form
                                launchSingleTop = true
                            }

                        }
                    }
                }
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Filled.Check, contentDescription = "Create")
                }
            }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = vm::onNameChange,
                placeholder = { Text("Full name") },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = state.contact,
                onValueChange = vm::onContactChange,
                placeholder = { Text("Email or phone") },
                label = { Text("Email / Phone (optional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Text(
                text = if (state.name.isBlank()) "Enter a name to enable Create"
                else "Tap ✓ to create and start chat",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
