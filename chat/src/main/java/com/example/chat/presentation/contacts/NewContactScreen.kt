package com.example.chat.presentation.contacts

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.example.chat.domain.model.Contact
import com.example.chat.domain.repository.ChatRepository
import com.example.chat.navigation.ChatRoutes
import com.example.chat.presentation.ui.Avatar
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// ----------------- UI State -----------------

data class NewContactUiState(
    val name: String = "",
    val contact: String = "",
    val isSaving: Boolean = false,
    val existing: List<Contact> = emptyList() // for duplicate hints
)

// ----------------- ViewModel -----------------

class NewContactViewModel(private val repo: ChatRepository) : ViewModel() {
    private val _state = MutableStateFlow(NewContactUiState())
    val state: StateFlow<NewContactUiState> = _state.asStateFlow()

    init {
        // keep existing contacts in state for duplicate checks
        viewModelScope.launch {
            repo.contacts().collect { all ->
                _state.update { it.copy(existing = all) }
            }
        }
    }

    fun onNameChange(v: String) = _state.update { it.copy(name = v) }
    fun onContactChange(v: String) = _state.update { it.copy(contact = v) }

    suspend fun createAndChat(): String {
        _state.update { it.copy(isSaving = true) }
        val id = repo.createContact(
            name = state.value.name.trim(),
            emailOrPhone = state.value.contact.trim().ifBlank { null }
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

// ----------------- Screen -----------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewContactScreen(nav: NavController, vm: NewContactViewModel) {
    val state by vm.state.collectAsState()
    val scope = rememberCoroutineScope()

    // --- validation helpers ---
    fun isValidEmail(s: String) = Patterns.EMAIL_ADDRESS.matcher(s).matches()
    fun isValidPhone(s: String) = s.filter(Char::isDigit).length == 10



    val nameOk by remember(state.name) {
        mutableStateOf(state.name.trim().isNotEmpty())
    }

    val contactTrim = state.contact.trim()
    val contactOk by remember(state.contact) {
        mutableStateOf(
            contactTrim.isBlank() || isValidEmail(contactTrim) || isValidPhone(contactTrim)
        )
    }

    val duplicateHint: String? = remember(state.existing, contactTrim) {
        if (contactTrim.isBlank()) null
        else {
            val dup = state.existing.any { it.emailOrPhone?.equals(contactTrim, ignoreCase = true) == true }
            if (dup) "A contact with the same info already exists. This will create another." else null
        }
    }

    val active = nameOk && contactOk && !state.isSaving

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
                    if (!active) return@FloatingActionButton
                    scope.launch {
                        val chatId = vm.createAndChat()
                        nav.navigate(ChatRoutes.detail(chatId)) {
                            popUpTo(ChatRoutes.HOME) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                containerColor = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (active) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.alpha(if (active) 1f else 0.6f)
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = if (active) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Icon(Icons.Filled.Check, contentDescription = "Create & chat")
                }
            }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header with live avatar from name
            Row(verticalAlignment = Alignment.CenterVertically) {
                Avatar(name = state.name.ifBlank { "?" }, url = null, size = 56.dp)
                Spacer(Modifier.width(12.dp))
                Text(
                    text = if (nameOk) state.name.trim() else "New contact",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Name
            OutlinedTextField(
                value = state.name,
                onValueChange = vm::onNameChange,
                placeholder = { Text("Full name") },
                label = { Text("Name *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                trailingIcon = {
                    if (state.name.isNotEmpty()) {
                        IconButton(onClick = { vm.onNameChange("") }) {
                            Icon(Icons.Filled.Close, contentDescription = "Clear name")
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                isError = !nameOk
            )

            // Email / Phone
            Column {
                OutlinedTextField(
                    value = state.contact,
                    onValueChange = vm::onContactChange,
                    placeholder = { Text("Email or phone (optional)") },
                    label = { Text("Email / Phone") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Outlined.AlternateEmail, contentDescription = null) },
                    trailingIcon = {
                        if (state.contact.isNotEmpty()) {
                            IconButton(onClick = { vm.onContactChange("") }) {
                                Icon(Icons.Filled.Close, contentDescription = "Clear contact")
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    isError = !contactOk
                )
                if (!contactOk) {
                    Text(
                        text = "Enter a valid email (name@host) or a 10+ digit phone number.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                } else if (duplicateHint != null) {
                    Text(
                        text = duplicateHint,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Helper
            Text(
                text = when {
                    !nameOk -> "Enter a name to enable Create"
                    !contactOk -> "Fix the contact field or leave it empty"
                    else -> "Tap ✓ to create and start chat"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
