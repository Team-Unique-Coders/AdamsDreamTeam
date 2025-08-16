package com.example.chat.presentation.home
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
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

    Scaffold(topBar = { TopAppBar(title = { Text("Chat") }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {

            TabRow(selectedTabIndex = tab) {
                Tab(selected = tab == 0, onClick = { tab = 0 }) { Text("Chats", Modifier.padding(16.dp)) }
                Tab(selected = tab == 1, onClick = { tab = 1 }) { Text("Contacts", Modifier.padding(16.dp)) }
                Tab(selected = tab == 2, onClick = { tab = 2 }) { Text("Calls", Modifier.padding(16.dp)) }
            }


            when (tab) {
                0 -> ChatListPane(nav = nav, vm = listVm)
                1 -> ContactsPane(nav = nav, vm = contactsVm)
                2 -> if (callsVm != null) {
                    CallsPane(nav = nav, vm = callsVm)
                } else {
                    Text("Calls (coming soon)", modifier = Modifier.padding(16.dp))
                }
            }

        }
    }
}
