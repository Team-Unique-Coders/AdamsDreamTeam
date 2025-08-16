package com.example.chat.presentation.ui

import java.text.SimpleDateFormat
import java.util.*

private val dayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

fun friendlyDate(ts: Long, now: Long = System.currentTimeMillis()): String {
    val cal = Calendar.getInstance().apply { timeInMillis = ts }
    val today = Calendar.getInstance().apply { timeInMillis = now }
    val yesterday = Calendar.getInstance().apply { timeInMillis = now; add(Calendar.DATE, -1) }

    fun Calendar.sameDay(other: Calendar) =
        get(Calendar.YEAR) == other.get(Calendar.YEAR) &&
                get(Calendar.DAY_OF_YEAR) == other.get(Calendar.DAY_OF_YEAR)

    return when {
        cal.sameDay(today) -> "Today"
        cal.sameDay(yesterday) -> "Yesterday"
        else -> dayFormat.format(Date(ts))
    }
}
