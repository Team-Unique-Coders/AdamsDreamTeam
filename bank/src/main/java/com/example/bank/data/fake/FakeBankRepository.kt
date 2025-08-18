package com.example.bank.data.fake

import com.example.bank.domain.model.Account
import com.example.bank.domain.model.AccountType
import com.example.bank.domain.repository.BankRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class FakeBankRepository : BankRepository {

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())

    override fun accounts(): StateFlow<List<Account>> = _accounts

    override suspend fun seedDemoData() {
        if (_accounts.value.isNotEmpty()) return // idempotent

        val checking = Account(
            id = UUID.randomUUID().toString(),
            name = "Everyday Checking",
            type = AccountType.CHECKING,
            balance = 2350.25,
            currency = "USD",
            maskedNumber = "•••• 1234"
        )
        val savings = Account(
            id = UUID.randomUUID().toString(),
            name = "Rainy Day Savings",
            type = AccountType.SAVINGS,
            balance = 1200.00,
            currency = "USD",
            maskedNumber = "•••• 5678"
        )

        _accounts.value = listOf(checking, savings)
    }

    // NEW: open a default Checking account (idempotent: returns existing if already opened)
    override suspend fun openCheckingAccount(): String {
        val existing = _accounts.value.firstOrNull { it.type == AccountType.CHECKING }
        if (existing != null) return existing.id

        val id = UUID.randomUUID().toString()
        val last4 = (1000..9999).random()
        val checking = Account(
            id = id,
            name = "Everyday Checking",
            type = AccountType.CHECKING,
            balance = 0.00,            // start at 0 for a realistic new account
            currency = "USD",
            maskedNumber = "•••• $last4"
        )
        _accounts.value = _accounts.value + checking
        return id
    }
}
