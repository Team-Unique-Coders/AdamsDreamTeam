package com.example.bank.domain.repository

import com.example.bank.domain.model.Account
import com.example.bank.domain.model.Transaction
import com.example.bank.domain.model.TxnType
import kotlinx.coroutines.flow.StateFlow

interface BankRepository {
    suspend fun seedDemoData()
    fun accounts(): StateFlow<List<Account>>
    suspend fun openCheckingAccount(): String
    fun transactions(): StateFlow<List<Transaction>>

    // NEW: create a transaction (sent = DEBIT, received = CREDIT) and return its id
    suspend fun addTransaction(
        accountId: String,
        type: TxnType,
        amount: Double,
        counterparty: String,
        note: String? = null
    ): String
}
