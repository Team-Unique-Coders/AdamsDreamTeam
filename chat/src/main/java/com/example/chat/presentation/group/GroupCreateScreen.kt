package com.example.chat.presentation.group

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.example.chat.domain.model.Contact
import com.example.chat.domain.repository.ChatRepository
import com.example.chat.navigation.ChatRoutes
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// ---------- ViewModel ----------

data class GroupCreateUiState(
    val contacts: List<Contact> = emptyList(),
    val selectedIds: Set<String> = emptySet(),
    val isLoading: Boolean = true
)

class GroupCreateViewModel(private val repo: ChatRepository) : ViewModel() {
    private val selectedIds = MutableStateFlow<Set<String>>(emptySet())

    private val contactsFlow: StateFlow<List<Contact>> =
        repo.contacts()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val state: StateFlow<GroupCreateUiState> =
        combine(contactsFlow, selectedIds) { contacts, sel ->
            GroupCreateUiState(contacts = contacts, selectedIds = sel, isLoading = false)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), GroupCreateUiState())

    fun toggle(id: String) {
        selectedIds.update { cur ->
            if (id in cur) cur - id else cur + id
        }
    }

    companion object {
        fun factory(repo: ChatRepository) = viewModelFactory {
            initializer { GroupCreateViewModel(repo) }
        }
    }
}

// ---------- Screen ----------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupCreateScreen(nav: NavController, vm: GroupCreateViewModel) {
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Group") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            if (state.selectedIds.size >= 2) {
                FloatingActionButton(
                    onClick = {
                        val members = state.selectedIds.toList().toTypedArray()
                        nav.navigate(ChatRoutes.groupTopic(*members))
                    }
                ) { Icon(Icons.Filled.Check, contentDescription = "Next") }
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {

            // Selected members preview (chips)
            if (state.selectedIds.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val names = state.contacts
                        .filter { it.id in state.selectedIds }
                        .joinToString(", ") { it.name }
                    Text("Participants: $names")
                }
            }

            if (state.isLoading) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.contacts) { contact ->
                    ContactSelectableRow(
                        contact = contact,
                        selected = contact.id in state.selectedIds,
                        onToggle = { vm.toggle(contact.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ContactSelectableRow(
    contact: Contact,
    selected: Boolean,
    onToggle: () -> Unit
) {
    ElevatedCard(onClick = onToggle, modifier = Modifier.fillMaxWidth()) {
        ListItem(
            headlineContent = { Text(contact.name) },
            supportingContent = { Text(contact.emailOrPhone ?: "") },
            trailingContent = {
                Checkbox(checked = selected, onCheckedChange = { onToggle() })
            }
        )
    }
}
