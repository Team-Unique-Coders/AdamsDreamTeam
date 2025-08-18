package com.example.bank.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.bank.presentation.onboarding.BankIntroScreen
import com.example.bank.presentation.onboarding.BankStartModeScreen
import com.example.bank.presentation.home.BankHomeScreen

fun NavGraphBuilder.bankGraph(
    navController: NavController
) {
    navigation(startDestination = BankRoutes.ONBOARDING_INTRO, route = BankRoutes.ROOT) {

        composable(BankRoutes.ONBOARDING_INTRO) {
            BankIntroScreen(
                onContinue = { navController.navigate(BankRoutes.ONBOARDING_START_MODE) },
                onExploreDemo = { navController.navigate(BankRoutes.HOME) } // temporary: no seeding yet
            )
        }

        composable(BankRoutes.ONBOARDING_START_MODE) {
            BankStartModeScreen(
                onUseDemo = {
                    // temporary: just navigate; we'll seed fake data next
                    navController.navigate(BankRoutes.HOME) {
                        popUpTo(BankRoutes.ONBOARDING_INTRO) { inclusive = true }
                    }
                },
                onConnectSimulated = { /* TODO: linking flow later */ },
                onBack = { navController.popBackStack() }
            )
        }

        composable(BankRoutes.HOME) {
            BankHomeScreen()
        }
    }
}
