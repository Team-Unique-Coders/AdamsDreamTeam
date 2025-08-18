package com.example.bank.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.bank.presentation.onboarding.BankIntroScreen
import com.example.bank.presentation.onboarding.BankStartModeScreen
import com.example.bank.presentation.home.BankHomeScreen

// ⬇️ NEW: onClose comes from the host (BankEntry), so the intro arrow can go Home
fun NavGraphBuilder.bankGraph(
    navController: NavController,
    onClose: (() -> Unit)? = null
) {
    navigation(startDestination = BankRoutes.ONBOARDING_INTRO, route = BankRoutes.ROOT) {

        composable(BankRoutes.ONBOARDING_INTRO) {
            BankIntroScreen(
                onContinue = { navController.navigate(BankRoutes.ONBOARDING_START_MODE) },
                onExploreDemo = { navController.navigate(BankRoutes.HOME) },
                onClose = onClose
            )
        }

        composable(BankRoutes.ONBOARDING_START_MODE) {
            BankStartModeScreen(
                onUseDemo = {
                    navController.navigate(BankRoutes.HOME) {
                        popUpTo(BankRoutes.ONBOARDING_INTRO) { inclusive = true }
                    }
                },
                onConnectSimulated = { /* TODO later */ },
                onBack = { navController.popBackStack() }
            )
        }

        composable(BankRoutes.HOME) {
            BankHomeScreen()
        }
    }
}
