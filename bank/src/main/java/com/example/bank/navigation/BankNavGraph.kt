// file: com/example/bank/navigation/BankNavGraph.kt
package com.example.bank.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.navArgument
import com.example.bank.domain.model.AccountType
import com.example.bank.domain.model.TxnType
import com.example.bank.domain.repository.BankRepository
import com.example.bank.presentation.filters.FiltersScreen
import com.example.bank.presentation.home.BankHomeScreen
import com.example.bank.presentation.onboarding.BankIntroScreen
import com.example.bank.presentation.onboarding.BankStartModeScreen
import com.example.bank.presentation.onboarding.CreateAccountScreen
import com.example.bank.presentation.send.SendConfirmScreen
import com.example.bank.presentation.send.SendMoneyScreen
import com.example.bank.presentation.send.SendSuccessScreen
import com.example.bank.presentation.transaction.TransactionDetailScreen
import kotlinx.coroutines.launch
import kotlin.math.roundToLong

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
            CreateAccountScreen(
                onBack = { navController.popBackStack() },
                onSubmit = { _, _, _ -> navController.navigate(BankRoutes.ONBOARDING_START_MODE) }
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
                onConnectSimulated = { /* later */ },
                onBack = { navController.popBackStack() }
            )
        }

        // Filters state in graph scope
        var monthsRange by mutableStateOf<IntRange?>(null)
        var categories by mutableStateOf<Set<String>>(emptySet())

        composable(BankRoutes.HOME) {
            val accounts by repo.accounts().collectAsState(emptyList())
            val transactions by repo.transactions().collectAsState(emptyList())
            val scope = rememberCoroutineScope()

            BankHomeScreen(
                accounts = accounts,
                transactions = transactions,
                selectedCategories = categories,
                monthsRange = monthsRange,
                onClose = onClose,
                onOpenChecking = { scope.launch { repo.openCheckingAccount() } },
                onOpenTransaction = { txnId ->
                    navController.navigate(BankRoutes.txnDetail(txnId))
                },
                onOpenFilters = { navController.navigate(BankRoutes.FILTERS) },
                onOpenSend = { navController.navigate(BankRoutes.SEND) } ,
                onOpenRequest = { /* navController.navigate(BankRoutes.REQUEST_MONEY) */ }
            )
        }

        composable(BankRoutes.FILTERS) {
            FiltersScreen(
                initialMonthsRange = monthsRange ?: (6..12),
                initialCategories = categories,
                onApply = { range, cats ->
                    monthsRange = range
                    categories = cats
                    navController.popBackStack()
                },
                onClear = {
                    monthsRange = null
                    categories = emptySet()
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ===================== SEND FLOW =====================

        // Step 1 — SendMoney
        composable(BankRoutes.SEND) {
            val contacts by repo.contacts().collectAsState(emptyList())
            SendMoneyScreen(
                contacts = contacts,
                onBack = { navController.popBackStack() },
                onNext = { contactId, amount, note ->
                    val cents = (amount * 100).roundToLong()
                    navController.navigate(BankRoutes.sendConfirm(contactId, cents)) {
                        launchSingleTop = true
                    }
                    // save note in currentBackStackEntry (optional)
                    navController.currentBackStackEntry?.savedStateHandle?.set("send_note", note)
                }
            )
        }

        // Step 2 — Confirm
        composable(
            route = BankRoutes.SEND_CONFIRM,
            arguments = listOf(
                navArgument("contactId") { type = NavType.StringType },
                navArgument("amountCents") { type = NavType.LongType }
            )
        ) { entry ->
            val contactId = entry.arguments?.getString("contactId")
            val amount = (entry.arguments?.getLong("amountCents") ?: 0L) / 100.0

            // Read note safely from previousBackStackEntry
            val note: String? = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<String?>("send_note")

            val contacts by repo.contacts().collectAsState(emptyList())
            val contact = contacts.firstOrNull { it.id == contactId }

            val accounts by repo.accounts().collectAsState(emptyList())
            val checking = accounts.firstOrNull { it.type == AccountType.CHECKING }
            val currentBalance = checking?.balance ?: 0.0

            if (contact == null) {
                // show loading or pop back if contact not found
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            } else {
                val scope = rememberCoroutineScope()

                SendConfirmScreen(
                    contact = contact,
                    amount = amount,
                    currentBalance = currentBalance,
                    onBack = { navController.popBackStack() },
                    onConfirmSend = {
                        val accId = checking?.id ?: return@SendConfirmScreen
                        scope.launch {
                            repo.addTransaction(
                                accountId = accId,
                                type = TxnType.DEBIT,
                                amount = amount,
                                counterparty = contact.name,
                                note = note
                            )
                            navController.navigate(BankRoutes.SEND_SUCCESS) {
                                launchSingleTop = true
                                popUpTo(BankRoutes.SEND) { inclusive = true }
                            }
                        }
                    }
                )
            }
        }
        // Step 3 — Success
        composable(BankRoutes.SEND_SUCCESS) {
            SendSuccessScreen(
                onGoHome = {
                    navController.navigate(BankRoutes.HOME) {
                        popUpTo(BankRoutes.ROOT) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }

        // =================== Transaction detail ===================
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
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
