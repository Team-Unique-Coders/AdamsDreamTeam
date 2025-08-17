package com.example.chat.presentation.group


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
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


data class GroupTopicUiState(
    val title: String = "",
    val members: List<String> = emptyList(),
    val isSaving: Boolean = false
)

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

    fun onTitleChange(v: String) = _state.update { it.copy(title = v) }

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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupTopicScreen(nav: NavController, vm: GroupTopicViewModel) {
    val state by vm.state.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add a Topic") },
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
                    if (!state.isSaving) {
                        scope.launch {
                            val chatId = vm.createGroup()
                            nav.navigate(ChatRoutes.detail(chatId)) {
                                popUpTo(ChatRoutes.HOME) { inclusive = false } // keep HOME, drop GROUP_CREATE/TOPIC
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
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = vm::onTitleChange,
                placeholder = { Text("Topic") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(12.dp))
            Text("Participants: ${state.members.size}")
        }
    }
}
