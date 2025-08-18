package com.example.chat.presentation.group

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Mood
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.example.chat.domain.model.Contact
import com.example.chat.domain.repository.ChatRepository
import com.example.chat.navigation.ChatRoutes
import com.example.chat.presentation.ui.Avatar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ---------- UI STATE ----------

data class GroupTopicUiState(
    val title: String = "",
    val members: List<String> = emptyList(),           // selected member IDs
    val memberContacts: List<Contact> = emptyList(),   // selected contacts (for chips/row)
    val allContacts: List<Contact> = emptyList(),      // for search sheet
    val isSaving: Boolean = false
)

// ---------- VIEWMODEL ----------

class GroupTopicViewModel(
    private val repo: ChatRepository,
    savedState: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(
        GroupTopicUiState(
            members = savedState.get<String>("members")
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?: emptyList()
        )
    )
    val state: StateFlow<GroupTopicUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repo.contacts().collect { all ->
                _state.update { s ->
                    val chosen = s.members.toSet()
                    s.copy(
                        allContacts = all,
                        memberContacts = all.filter { it.id in chosen }
                    )
                }
            }
        }
    }


    fun onTitleChange(v: String) = _state.update { it.copy(title = v) }

    fun toggleMember(id: String) {
        _state.update { s ->
            val set = s.members.toMutableSet()
            if (!set.add(id)) set.remove(id)
            val updated = set.toList()
            s.copy(
                members = updated,
                memberContacts = s.allContacts.filter { it.id in updated.toSet() }
            )
        }
    }


    suspend fun createGroup(): String {
        _state.update { it.copy(isSaving = true) }
        val id = repo.createGroupChat(
            title = state.value.title.ifBlank { "New Group" },
            memberIds = state.value.members
        )
        _state.update { it.copy(isSaving = false) }
        return id
    }

    companion object {
        fun factory(repo: ChatRepository) = viewModelFactory {
            initializer { GroupTopicViewModel(repo, this.createSavedStateHandle()) }
        }
    }
}

// ---------- SCREEN ----------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupTopicScreen(nav: NavController, vm: GroupTopicViewModel) {
    val state by vm.state.collectAsState()
    val scope = rememberCoroutineScope()
    val minMembers = 3
    val canConfirm = state.title.isNotBlank() && state.members.size >= minMembers && !state.isSaving

    var showSearch by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add a Topic") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showSearch = true }) {
                        Icon(Icons.Outlined.Search, contentDescription = "Search contacts")
                    }
                }
            )
        },
        floatingActionButton = {
            val active = canConfirm && !state.isSaving
            FloatingActionButton(
                onClick = {
                    if (!active) return@FloatingActionButton
                    scope.launch {
                        val chatId = vm.createGroup()
                        nav.navigate(ChatRoutes.detail(chatId)) {
                            popUpTo(ChatRoutes.HOME) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                containerColor = if (active) Color(0xFF23C16B) else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (active) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.alpha(if (active) 1f else 0.6f)
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = if (active) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Icon(Icons.Filled.Check, contentDescription = "Create")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Camera bubble + topic field with emoji inside
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable { /* TODO choose group icon */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.PhotoCamera,
                        contentDescription = "Group icon",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(Modifier.width(12.dp))
                OutlinedTextField(
                    value = state.title,
                    onValueChange = vm::onTitleChange,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Topic") },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { /* TODO emoji */ }) {
                            Icon(Icons.Outlined.Mood, contentDescription = "Emoji")
                        }
                    },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                        onDone = {
                            if (canConfirm) {
                                scope.launch {
                                    val chatId = vm.createGroup()
                                    nav.navigate(ChatRoutes.detail(chatId)) {
                                        popUpTo(ChatRoutes.HOME) { inclusive = false }
                                        launchSingleTop = true
                                    }
                                }
                            }
                        }
                    )
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Please give a topic to the group and possibly an icon to the group",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Participants : ${state.memberContacts.size} (min $minMembers)",
                style = MaterialTheme.typography.labelLarge
            )
            if (state.memberContacts.size < minMembers) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Select at least $minMembers people to create a group.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }


            Spacer(Modifier.height(12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(state.memberContacts, key = { it.id }) { c ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Avatar(name = c.name, url = c.avatarUrl, size = 56.dp)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = c.name,
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }

    // ---- Search bottom sheet ----
    if (showSearch) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        var query by remember { mutableStateOf("") }

        ModalBottomSheet(
            onDismissRequest = { showSearch = false },
            sheetState = sheetState
        ) {
            Column(Modifier.fillMaxWidth().padding(16.dp)) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search contacts") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) }
                )
                Spacer(Modifier.height(12.dp))

                val filtered = remember(state.allContacts, query) {
                    val q = query.trim().lowercase()
                    if (q.isEmpty()) state.allContacts
                    else state.allContacts.filter { c ->
                        c.name.lowercase().contains(q) ||
                                (c.emailOrPhone?.lowercase()?.contains(q) == true)
                    }
                }

                LazyColumn(
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filtered, key = { it.id }) { c ->
                        val selected = c.id in state.members
                        ElevatedCard(
                            onClick = { vm.toggleMember(c.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ListItem(
                                leadingContent = { Avatar(name = c.name, url = c.avatarUrl) },
                                headlineContent = { Text(c.name) },
                                supportingContent = { Text(c.emailOrPhone ?: "") },
                                trailingContent = {
                                    Checkbox(
                                        checked = selected,
                                        onCheckedChange = { vm.toggleMember(c.id) }
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
