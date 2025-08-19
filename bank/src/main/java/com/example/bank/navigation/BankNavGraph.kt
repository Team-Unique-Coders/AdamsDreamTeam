// file: com/example/bank/navigation/BankNavGraph.kt
package com.example.bank.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.bank.domain.repository.BankRepository
import com.example.bank.presentation.home.BankHomeScreen
import com.example.bank.presentation.onboarding.BankIntroScreen
import com.example.bank.presentation.onboarding.BankStartModeScreen
import com.example.bank.presentation.onboarding.CreateAccountScreen
import com.example.bank.presentation.transaction.TransactionDetailScreen
import kotlinx.coroutines.launch

fun NavGraphBuilder.bankGraph(
    navController: NavController,
    onClose: (() -> Unit)? = null,
    repo: BankRepository
) {
    // If you want to skip onboarding, change startDestination to BankRoutes.HOME
    navigation(startDestination = BankRoutes.ONBOARDING_INTRO, route = BankRoutes.ROOT) {

        composable(BankRoutes.ONBOARDING_INTRO) {
            BankIntroScreen(
                onContinue = { navController.navigate(BankRoutes.ONBOARDING_CREATE_ACCOUNT) },
                onExploreDemo = { navController.navigate(BankRoutes.HOME) },
                onClose = onClose
            )
        }

        composable(BankRoutes.ONBOARDING_CREATE_ACCOUNT) {
            CreateAccountScreen(
                onBack = { navController.popBackStack() },
                onSubmit = { _, _, _ ->
                    navController.navigate(BankRoutes.ONBOARDING_START_MODE)
                }
            )
        }

        composable(BankRoutes.ONBOARDING_START_MODE) {
            val scope = rememberCoroutineScope()
            BankStartModeScreen(
                onUseDemo = {
                    scope.launch {
                        repo.openCheckingAccount()
                        navController.navigate(BankRoutes.HOME) {
                            popUpTo(BankRoutes.ONBOARDING_INTRO) { inclusive = true }
                        }
                    }
                },
                onConnectSimulated = { /* linking flow later */ },
                onBack = { navController.popBackStack() }
            )
        }

        composable(BankRoutes.HOME) {
            val accounts by repo.accounts().collectAsState(emptyList())
            val transactions by repo.transactions().collectAsState(emptyList())
            val scope = rememberCoroutineScope()

            BankHomeScreen(
                accounts = accounts,
                transactions = transactions,
                onClose = onClose,
                onOpenChecking = { scope.launch { repo.openCheckingAccount() } },
                onOpenTransaction = { txnId ->
                    navController.navigate(BankRoutes.txnDetail(txnId))
                }
            )
        }

        composable(
            route = BankRoutes.TXN_DETAIL,
            arguments = listOf(navArgument("txnId") { type = NavType.StringType })
        ) { backStackEntry ->
            val txnId = backStackEntry.arguments?.getString("txnId")
            val transactions by repo.transactions().collectAsState(emptyList())
            val txn = transactions.firstOrNull { it.id == txnId }

            if (txn != null) {
                TransactionDetailScreen(
                    txn = txn,
                    onBack = { navController.popBackStack() }
                )
            } else {
                // Instead of popping instantly, show loading
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            }
        }

    }
}
