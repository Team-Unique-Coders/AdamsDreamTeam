// file: com/example/bank/navigation/BankRoutes.kt
package com.example.bank.navigation

object BankRoutes {
    const val ROOT = "bank"

    // Onboarding
    const val ONBOARDING_INTRO = "bank/onboarding/intro"
    const val ONBOARDING_CREATE_ACCOUNT = "bank/onboarding/create"
    const val ONBOARDING_START_MODE = "bank/onboarding/start"

    // Home
    const val HOME = "bank/home"
    const val FILTERS = "bank/filters"

    // Transaction detail
    const val TXN_DETAIL = "bank/txn/{txnId}"
    fun txnDetail(txnId: String) = "bank/txn/$txnId"

    // ✅ NEW: Send flow
    const val SEND = "bank/send"
    const val SEND_CONFIRM = "bank/send/confirm/{contactId}/{amountCents}"
    const val SEND_SUCCESS = "bank/send/success"
    fun sendConfirm(contactId: String, amountCents: Long) =
        "bank/send/confirm/$contactId/$amountCents"
}
