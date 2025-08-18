package com.example.bank.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.bank.presentation.onboarding.BankIntroScreen
import com.example.bank.presentation.onboarding.BankStartModeScreen
import com.example.bank.presentation.onboarding.CreateAccountScreen
import com.example.bank.presentation.home.BankHomeScreen
import com.example.bank.domain.repository.BankRepository
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

fun NavGraphBuilder.bankGraph(
    navController: NavController,
    onClose: (() -> Unit)? = null,
    repo: BankRepository
) {
    navigation(startDestination = BankRoutes.ONBOARDING_INTRO, route = BankRoutes.ROOT) {

        composable(BankRoutes.ONBOARDING_INTRO) {
            BankIntroScreen(
                onContinue = { navController.navigate(BankRoutes.ONBOARDING_CREATE_ACCOUNT) },
                onExploreDemo = { navController.navigate(BankRoutes.HOME) },
                onClose = onClose
            )
        }

        composable(BankRoutes.ONBOARDING_CREATE_ACCOUNT) {
            val scope = rememberCoroutineScope()
            CreateAccountScreen(
                onBack = { navController.popBackStack() },
                onSubmit = { _, _, _ ->
                    scope.launch {
                        // Open a real product: Checking account (in-memory)
                        repo.openCheckingAccount()
                        navController.navigate(BankRoutes.HOME) {
                            popUpTo(BankRoutes.ONBOARDING_INTRO) { inclusive = true }
                        }
                    }
                }
            )
        }

        // (Optional) Start Mode kept for future linking flow
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
            val accounts by repo.accounts().collectAsState(emptyList())
            BankHomeScreen(accounts = accounts)
        }
    }
}
