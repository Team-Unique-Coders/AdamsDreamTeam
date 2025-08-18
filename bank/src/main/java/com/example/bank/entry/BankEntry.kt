package com.example.bank.entry

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.bank.navigation.BankRoutes
import com.example.bank.navigation.bankGraph

@Composable
fun BankEntry(onClose: (() -> Unit)? = null) {
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = BankRoutes.ROOT
    ) {
        bankGraph(navController = nav)
    }
}
