package com.example.bank.domain.repository

import com.example.bank.domain.model.Account
import com.example.bank.domain.model.Contact
import com.example.bank.domain.model.Transaction
import com.example.bank.domain.model.TxnType
import kotlinx.coroutines.flow.StateFlow

interface BankRepository {
    // Streams
    fun accounts(): StateFlow<List<Account>>
    fun transactions(): StateFlow<List<Transaction>>
    fun contacts(): StateFlow<List<Contact>>

    // Setup / mutations
    suspend fun seedDemoData()
    suspend fun openCheckingAccount(): String

    suspend fun addTransaction(
        accountId: String,
        type: TxnType,
        amount: Double,
        counterparty: String,
        note: String? = null
    ): String
}