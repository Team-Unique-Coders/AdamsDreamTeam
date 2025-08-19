// file: com/example/bank/data/fake/FakeBankRepository.kt
package com.example.bank.data.fake

import com.example.bank.domain.model.Account
import com.example.bank.domain.model.AccountType
import com.example.bank.domain.model.Transaction
import com.example.bank.domain.model.TxnStatus
import com.example.bank.domain.model.TxnType
import com.example.bank.domain.model.Contact
import com.example.bank.domain.repository.BankRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeBankRepository : BankRepository {

    private val _accounts       = MutableStateFlow<List<Account>>(emptyList())
    private val _transactions   = MutableStateFlow<List<Transaction>>(emptyList())
    private val _contacts       = MutableStateFlow<List<Contact>>(emptyList())

    override fun accounts(): StateFlow<List<Account>>       = _accounts
    override fun transactions(): StateFlow<List<Transaction>> = _transactions
    override fun contacts(): StateFlow<List<Contact>>       = _contacts   // ← NEW

    // deterministic base (Aug 15, 2025 12:00:00 UTC)
    private companion object {
        private const val BASE_MS: Long = 1_726_720_000_000L
        private fun h(hours: Int) = hours * 60 * 60 * 1000L
    }

    /** Seed deterministic demo data exactly once (no switching order). */
    override suspend fun seedDemoData() {
        if (_accounts.value.isNotEmpty()) return // idempotent

        // --- Accounts (fixed ids) ---
        val checking = Account(
            id = "acc_checking",
            name = "Everyday Checking",
            type = AccountType.CHECKING,
            balance = 0.0,
            currency = "USD",
            maskedNumber = "•••• 1234"
        )
        val savings = Account(
            id = "acc_savings",
            name = "Rainy Day Savings",
            type = AccountType.SAVINGS,
            balance = 0.0,
            currency = "USD",
            maskedNumber = "•••• 5678"
        )
        _accounts.value = listOf(checking, savings)

        // --- Contacts (fixed ids & order) ---
        _contacts.value = listOf(
            Contact(id = "c_rebecca", name = "Rebecca Moore",   email = "rebecca@gmail.com",    phone = "5551001"),
            Contact(id = "c_franz",   name = "Franz Ferdinand", email = "franz@mail.com",       phone = "5551002"),
            Contact(id = "c_buddy",   name = "My Buddyz",       email = null,                   phone = "5551003"),
            Contact(id = "c_burger",  name = "Burger Delivery", email = "support@burger.com",   phone = null),
            Contact(id = "c_alice",   name = "Alice Park",      email = "alice@park.co",        phone = "5551010"),
            Contact(id = "c_david",   name = "David Jones",     email = null,                   phone = "5551011"),
            Contact(id = "c_uber",    name = "Uber",            email = "help@uber.com",        phone = null),
            Contact(id = "c_spotify", name = "Spotify",         email = "support@spotify.com",  phone = null)
        )

        // --- Start balances so totals look real ---
        val startChecking = 2000.00
        val startSavings  = 1200.00

        // --- Transactions (deterministic timestamps, newest → oldest) ---
        val seeded = listOf(
            Transaction(
                id = "txn_rebecca",
                accountId = checking.id,
                type = TxnType.DEBIT,
                amount = 72.00,
                currency = "USD",
                counterparty = "Rebecca Moore",
                timestampMs = BASE_MS + h(6),
                status = TxnStatus.POSTED,
                note = null
            ),
            Transaction(
                id = "txn_spotify",
                accountId = checking.id,
                type = TxnType.DEBIT,
                amount = 14.99,
                currency = "USD",
                counterparty = "Spotify",
                timestampMs = BASE_MS + h(5),
                status = TxnStatus.POSTED,
                note = null
            ),
            Transaction(
                id = "txn_franz_1",
                accountId = checking.id,
                type = TxnType.CREDIT,
                amount = 125.00,
                currency = "USD",
                counterparty = "Franz Ferdinand",
                timestampMs = BASE_MS - h(20),
                status = TxnStatus.POSTED,
                note = null
            ),
            Transaction(
                id = "txn_franz_2",
                accountId = checking.id,
                type = TxnType.CREDIT,
                amount = 125.00,
                currency = "USD",
                counterparty = "Franz Ferdinand",
                timestampMs = BASE_MS - h(22),
                status = TxnStatus.POSTED,
                note = null
            ),
            Transaction(
                id = "txn_iclickigo",
                accountId = checking.id,
                type = TxnType.DEBIT,
                amount = 9.50,
                currency = "USD",
                counterparty = "iclickigo",
                timestampMs = BASE_MS - h(28),
                status = TxnStatus.POSTED,
                note = null
            ),
            Transaction(
                id = "txn_savings_refund",
                accountId = savings.id,
                type = TxnType.CREDIT,
                amount = 200.00,
                currency = "USD",
                counterparty = "Refund",
                timestampMs = BASE_MS - h(48),
                status = TxnStatus.POSTED,
                note = null
            )
        )
        _transactions.value = seeded.sortedByDescending { it.timestampMs }

        // --- Compute balances once from start + net(txns) ---
        val netChecking = _transactions.value
            .filter { it.accountId == checking.id }
            .sumOf { if (it.type == TxnType.CREDIT) it.amount else -it.amount }
        val netSavings = _transactions.value
            .filter { it.accountId == savings.id }
            .sumOf { if (it.type == TxnType.CREDIT) it.amount else -it.amount }

        _accounts.value = listOf(
            checking.copy(balance = startChecking + netChecking),
            savings.copy(balance = startSavings + netSavings)
        )
    }

    override suspend fun openCheckingAccount(): String {
        val existing = _accounts.value.firstOrNull { it.type == AccountType.CHECKING }
        if (existing != null) return existing.id

        val id = "acc_checking"
        val checking = Account(
            id = id,
            name = "Everyday Checking",
            type = AccountType.CHECKING,
            balance = 0.00,
            currency = "USD",
            maskedNumber = "•••• 1234"
        )
        _accounts.value = _accounts.value + checking

        // starter txns (still deterministic)
        addInternal(
            Transaction(
                id = "txn_rebecca",
                accountId = id,
                type = TxnType.DEBIT,
                amount = 72.00,
                currency = "USD",
                counterparty = "Rebecca Moore",
                timestampMs = BASE_MS + h(6),
                status = TxnStatus.POSTED,
                note = null
            )
        )
        addInternal(
            Transaction(
                id = "txn_franz_1",
                accountId = id,
                type = TxnType.CREDIT,
                amount = 125.00,
                currency = "USD",
                counterparty = "Franz Ferdinand",
                timestampMs = BASE_MS - h(20),
                status = TxnStatus.POSTED,
                note = null
            )
        )
        return id
    }

    override suspend fun addTransaction(
        accountId: String,
        type: TxnType,
        amount: Double,
        counterparty: String,
        note: String?
    ): String {
        val acc = _accounts.value.firstOrNull { it.id == accountId }
            ?: throw IllegalArgumentException("Account not found: $accountId")

        val txn = Transaction(
            id = "txn_" + System.nanoTime().toString(36),
            accountId = accountId,
            type = type,
            amount = amount.coerceAtLeast(0.0),
            currency = acc.currency,
            counterparty = counterparty,
            timestampMs = System.currentTimeMillis(),
            status = TxnStatus.POSTED,
            note = note
        )
        addInternal(txn)
        return txn.id
    }

    // ---- helpers ----

    private fun addInternal(txn: Transaction) {
        _transactions.value = (_transactions.value.filterNot { it.id == txn.id } + txn)
            .sortedByDescending { it.timestampMs }

        _accounts.value = _accounts.value.map { acc ->
            if (acc.id == txn.accountId) {
                val delta = if (txn.type == TxnType.CREDIT) txn.amount else -txn.amount
                acc.copy(balance = acc.balance + delta)
            } else acc
        }
    }
}
