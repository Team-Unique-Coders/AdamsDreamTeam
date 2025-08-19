package com.example.bank.domain.model

enum class TxnType { DEBIT, CREDIT }          // money out / money in
enum class TxnStatus { POSTED, PENDING }

data class Transaction(
    val id: String,
    val accountId: String,                    // which money account this belongs to
    val type: TxnType,                        // DEBIT (sent) / CREDIT (received)
    val amount: Double,                       // keep Double for demo; can swap later
    val currency: String,                     // e.g., "USD"
    val counterparty: String,                 // who you paid or received from
    val timestampMs: Long,                    // System.currentTimeMillis()
    val status: TxnStatus = TxnStatus.POSTED,
    val note: String? = null
)
