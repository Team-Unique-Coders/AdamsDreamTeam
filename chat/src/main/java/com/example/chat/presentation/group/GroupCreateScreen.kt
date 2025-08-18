package com.example.chat.presentation.group

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupCreateScreen(nav: NavController, vm: GroupCreateViewModel) {
    val state by vm.state.collectAsState()
    val minMembers = 3


    // search state
    var showSearch by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    // filtered list
    val filtered = remember(state.contacts, query) {
        val q = query.trim().lowercase()
        if (q.isEmpty()) state.contacts
        else state.contacts.filter { c ->
            c.name.lowercase().contains(q) ||
                    (c.emailOrPhone?.lowercase()?.contains(q) == true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Group") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showSearch = !showSearch }) {
                        Icon(Icons.Outlined.Search, contentDescription = "Search")
                    }
                }
            )
        },
        floatingActionButton = {
            if (state.selectedIds.size >= minMembers) {
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

            // Selected members preview (chips / simple text)
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

            if (state.selectedIds.size < minMembers) {
                Text(
                    text = "Select at least $minMembers people to create a group.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Search box (toggleable)
            if (showSearch) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    singleLine = true,
                    placeholder = { Text("Search contacts") },
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { query = "" }) {
                                Icon(Icons.Filled.Close, contentDescription = "Clear")
                            }
                        }
                    }
                )
                Spacer(Modifier.height(8.dp))
            }

            if (state.isLoading) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }

            // List (uses filtered instead of state.contacts)
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filtered) { contact ->
                    ContactSelectableRow(
                        contact = contact,
                        selected = contact.id in state.selectedIds,
                        onToggle = { vm.toggle(contact.id) }
                    )
                }

                if (!state.isLoading && filtered.isEmpty()) {
                    item {
                        Text(
                            "No contacts match your search.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
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
