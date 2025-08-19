// file: com/example/bank/entry/BankEntry.kt
package com.example.bank.entry

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.bank.data.fake.FakeBankRepository
import com.example.bank.navigation.BankRoutes
import com.example.bank.navigation.bankGraph

@Composable
fun BankEntry(onClose: (() -> Unit)? = null) {
    val nav = rememberNavController()
    val repo = remember { FakeBankRepository() }

    // Seed once so Home has content
    LaunchedEffect(Unit) { repo.seedDemoData() }

    NavHost(
        navController = nav,
        startDestination = BankRoutes.ROOT
    ) {
        bankGraph(
            navController = nav,
            onClose = onClose,
            repo = repo
        )
    }
}
