// file: com/example/bank/domain/model/Contact.kt
package com.example.bank.domain.model

/**
 * Lightweight contact used by Send/Request flows.
 */
data class Contact(
    val id: String,
    val name: String,
    val email: String? = null,
    val phone: String? = null,
    /** Optional remote/local avatar url/path if you ever add images. */
    val avatarUrl: String? = null
) {
    /** Convenience initials for placeholder avatars. */
    val initials: String
        get() {
            val parts = name.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
            return when (parts.size) {
                0 -> ""
                1 -> parts[0].take(2).uppercase()
                else -> "${parts[0].first().uppercaseChar()}${parts[1].first().uppercaseChar()}"
            }
        }
}
