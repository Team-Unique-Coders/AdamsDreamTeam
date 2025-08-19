package com.example.bank.domain.model

// Keeping it tiny for now.
// We'll add Transaction later in its own file.
enum class AccountType { CHECKING, SAVINGS, WALLET }

data class Account(
    val id: String,
    val name: String,
    val type: AccountType,
    val balance: Double,     // fine for demo; we can switch to BigDecimal later
    val currency: String,    // e.g., "USD"
    val maskedNumber: String // e.g., "•••• 1234"
)
