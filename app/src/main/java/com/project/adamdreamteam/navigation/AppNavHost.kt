package com.project.adamdreamteam.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bank.BankScreen
import com.example.chat.entry.ChatEntry
import com.project.adamdreamteam.ui.home.HomePage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier
    ) {
        // Home hub
        composable(Routes.HOME) {
            HomePage(onOpen = { route -> navController.navigate(route) })
        }

        composable(Routes.UBER) { StubScreen("Uber") }
        composable(Routes.TINDER) { StubScreen("Tinder") }
        composable(Routes.DELIVERY) { StubScreen("Delivery") }
        composable(Routes.LEARN) { StubScreen("Learn") }
        composable(Routes.CHAT) {
            ChatEntry(
                onClose = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.DOCTOR) { StubScreen("Doctor") }
        composable(Routes.LAUNDRY) { StubScreen("Laundry") }
        composable(Routes.EAT) { StubScreen("Eat") }
        composable(Routes.HOTEL) { StubScreen("Hotel") }
        composable(Routes.HANDYMAN) { StubScreen("Handyman") }
        composable(Routes.MECHANIC) { StubScreen("Mechanic") }


        composable(Routes.BANK) {
            BankScreen(
                onBackToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StubScreen(title: String) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(title) }) }
    ) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
        )
    }
}
