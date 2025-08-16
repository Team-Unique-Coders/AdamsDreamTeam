package com.example.chat.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.example.chat.domain.repository.ChatRepository
import com.example.chat.presentation.contacts.ContactsScreen
import com.example.chat.presentation.contacts.ContactsViewModel
import com.example.chat.presentation.detail.ChatDetailScreen
import com.example.chat.presentation.detail.ChatDetailViewModel
import com.example.chat.presentation.list.ChatListScreen
import com.example.chat.presentation.list.ChatListViewModel

object ChatRoutes {
    const val ROOT = "chat"
    const val HOME = "chat/home"           // ← add this line
    const val LIST = "chat/list"
    const val CONTACTS = "chat/contacts"
    const val DETAIL = "chat/detail/{chatId}"



        // NEW:
    const val GROUP_CREATE = "chat/group/create"
    const val GROUP_TOPIC = "chat/group/topic/{members}"

    const val NEW_CONTACT = "chat/contact/new"

    fun detail(chatId: String) = "chat/detail/$chatId"
    fun groupTopic(vararg memberIds: String) =
        "chat/group/topic/${memberIds.joinToString(",")}"

}


/**
 * Register the Chat graph inside your App's NavHost.
 * Usage from your app NavHost (a @Composable):
 *   val chatRepo = remember { FakeChatRepository() }
 *   chatGraph(navController, chatRepo)
 */

fun NavGraphBuilder.chatGraph(
    navController: NavController,
    repo: ChatRepository
) {
    navigation(startDestination = ChatRoutes.LIST, route = ChatRoutes.ROOT) {

        composable(ChatRoutes.LIST) {
            val vm: ChatListViewModel = viewModel(factory = ChatListViewModel.factory(repo))
            ChatListScreen(nav = navController, vm = vm)
        }

        composable(ChatRoutes.CONTACTS) {
            val vm: ContactsViewModel = viewModel(factory = ContactsViewModel.factory(repo))
            ContactsScreen(nav = navController, vm = vm)
        }

        composable(
            route = ChatRoutes.DETAIL,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) {
            val vm: ChatDetailViewModel = viewModel(factory = ChatDetailViewModel.factory(repo))
            ChatDetailScreen(nav = navController, vm = vm)
        }
    }
}
