package com.example.chat.entry

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chat.data.fake.FakeChatRepository
import com.example.chat.navigation.ChatRoutes
import com.example.chat.presentation.calls.CallsViewModel
import com.example.chat.presentation.home.ChatHomeScreen
import com.example.chat.presentation.list.ChatListViewModel
import com.example.chat.presentation.contacts.ContactsViewModel
import com.example.chat.presentation.contacts.NewContactScreen
import com.example.chat.presentation.contacts.NewContactViewModel
import com.example.chat.presentation.detail.ChatDetailScreen
import com.example.chat.presentation.detail.ChatDetailViewModel
import com.example.chat.presentation.group.GroupCreateScreen
import com.example.chat.presentation.group.GroupCreateViewModel
import com.example.chat.presentation.group.GroupTopicScreen
import com.example.chat.presentation.group.GroupTopicViewModel

@Composable
fun ChatEntry() {
    val nav = rememberNavController()
    val repo = remember { FakeChatRepository() } // one instance for the whole flow

    NavHost(navController = nav, startDestination = ChatRoutes.HOME) {

        // Tabs page (Chats / Contacts)
        composable(ChatRoutes.HOME) {
            val listVm: ChatListViewModel = viewModel(factory = ChatListViewModel.factory(repo))
            val contactsVm: ContactsViewModel = viewModel(factory = ContactsViewModel.factory(repo))
            val callsVm: CallsViewModel = viewModel(factory = CallsViewModel.factory(repo)) // ← add

            ChatHomeScreen(
                nav = nav,
                listVm = listVm,
                contactsVm = contactsVm,
                callsVm = callsVm                                      // ← pass
            )
        }


        // Group creation: select members
        composable(ChatRoutes.GROUP_CREATE) {
            val vm: GroupCreateViewModel = viewModel(factory = GroupCreateViewModel.factory(repo))
            GroupCreateScreen(nav = nav, vm = vm)
        }

        // Group creation: set topic/name
        composable(
            route = ChatRoutes.GROUP_TOPIC,
            arguments = listOf(navArgument("members") { type = NavType.StringType })
        ) {
            val vm: GroupTopicViewModel = viewModel(factory = GroupTopicViewModel.factory(repo))
            GroupTopicScreen(nav = nav, vm = vm)
        }

        // Conversation page
        composable(
            route = ChatRoutes.DETAIL,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) {
            val vm: ChatDetailViewModel = viewModel(factory = ChatDetailViewModel.factory(repo))
            ChatDetailScreen(nav = nav, vm = vm)
        }

        // New contact form
        composable(ChatRoutes.NEW_CONTACT) {
            val vm: NewContactViewModel = viewModel(factory = NewContactViewModel.factory(repo))
            NewContactScreen(nav = nav, vm = vm)
        }

    }
}
