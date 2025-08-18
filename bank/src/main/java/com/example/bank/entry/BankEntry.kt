package com.example.bank.entry

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.bank.navigation.BankRoutes
import com.example.bank.navigation.bankGraph
import com.example.bank.data.fake.FakeBankRepository

@Composable
fun BankEntry(onClose: (() -> Unit)? = null) {
    val nav = rememberNavController()

    // In-memory repository instance for the whole Bank flow
    val repo = remember { FakeBankRepository() }

    NavHost(
        navController = nav,
        startDestination = BankRoutes.ROOT
    ) {
        // ⬇️ pass repo so Start Mode can seed demo data
        bankGraph(navController = nav, onClose = onClose, repo = repo)
    }
}
