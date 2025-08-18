package com.example.bank.domain.repository

import com.example.bank.domain.model.Account
import kotlinx.coroutines.flow.StateFlow

interface BankRepository {
    /** Create demo accounts/balances once (kept for dev tools if you want). */
    suspend fun seedDemoData()

    /** Live stream of accounts for the Home screen. */
    fun accounts(): StateFlow<List<Account>>

    /** Open a default Checking account and return its id. */
    suspend fun openCheckingAccount(): String
}
