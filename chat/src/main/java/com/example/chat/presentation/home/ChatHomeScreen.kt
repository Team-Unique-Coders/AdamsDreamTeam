package com.example.chat.presentation.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chat.presentation.calls.CallsPane
import com.example.chat.presentation.calls.CallsViewModel
import com.example.chat.presentation.contacts.ContactsPane
import com.example.chat.presentation.contacts.ContactsViewModel
import com.example.chat.presentation.list.ChatListPane
import com.example.chat.presentation.list.ChatListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatHomeScreen(
    nav: NavController,
    listVm: ChatListViewModel,
    contactsVm: ContactsViewModel,
    callsVm: CallsViewModel? = null
) {
    var tab by rememberSaveable { mutableStateOf(0) }

    // per-tab queries
    var chatsQuery by rememberSaveable { mutableStateOf("") }
    var contactsQuery by rememberSaveable { mutableStateOf("") }
    var callsQuery by rememberSaveable { mutableStateOf("") }

    var isSearching by rememberSaveable { mutableStateOf(false) }

    val kb = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    // Back button exits search first
    BackHandler(enabled = isSearching) {
        isSearching = false
        kb?.hide()
        focusManager.clearFocus()
    }

    // Focus the field when search opens; hide keyboard when it closes
    LaunchedEffect(isSearching, tab) {
        if (isSearching) {
            focusRequester.requestFocus()
            kb?.show()
        } else {
            kb?.hide()
            focusManager.clearFocus()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat") },
                actions = {
                    IconButton(onClick = { isSearching = !isSearching }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Tabs
            TabRow(selectedTabIndex = tab) {
                Tab(selected = tab == 0, onClick = { tab = 0 }) { Text("Chats", Modifier.padding(16.dp)) }
                Tab(selected = tab == 1, onClick = { tab = 1 }) { Text("Contacts", Modifier.padding(16.dp)) }
                Tab(selected = tab == 2, onClick = { tab = 2 }) { Text("Calls", Modifier.padding(16.dp)) }
            }

            // Search field for active tab
            if (isSearching) {
                val (value, setter, placeholder) = when (tab) {
                    0 -> Triple(chatsQuery, { v: String -> chatsQuery = v }, "Search chats")
                    1 -> Triple(contactsQuery, { v: String -> contactsQuery = v }, "Search contacts")
                    else -> Triple(callsQuery, { v: String -> callsQuery = v }, "Search calls")
                }

                OutlinedTextField(
                    value = value,
                    onValueChange = setter,
                    placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    singleLine = true,
                    trailingIcon = {
                        if (value.isNotEmpty()) {
                            IconButton(onClick = { setter("") }) {
                                Icon(Icons.Filled.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { kb?.hide() })
                )
            }

            // Content area with a tap-catcher overlay when searching
            Box(Modifier.fillMaxSize()) {
                // Actual content
                when (tab) {
                    0 -> ChatListPane(
                        nav = nav,
                        vm = listVm,
                        query = if (isSearching) chatsQuery else ""
                    )
                    1 -> ContactsPane(
                        nav = nav,
                        vm = contactsVm,
                        query = if (isSearching) contactsQuery else ""
                    )
                    2 -> if (callsVm != null) {
                        CallsPane(
                            nav = nav,
                            vm = callsVm,
                            query = if (isSearching) callsQuery else ""
                        )
                    } else {
                        Text("Calls (coming soon)", modifier = Modifier.padding(16.dp))
                    }
                }

                // Transparent overlay that closes search on first tap (doesn't cover the search field)
                if (isSearching) {
                    Box(
                        Modifier
                            .matchParentSize()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                isSearching = false
                                kb?.hide()
                                focusManager.clearFocus()
                            }
                    )
                }
            }
        }
    }
}
