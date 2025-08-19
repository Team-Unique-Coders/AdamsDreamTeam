package com.example.handyman.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/**
 * Simple in-memory store for the feature. No DI, no persistence.
 * You can swap this for DataStore/Room later if you want true persistence.
 */
object HandymanStore {

    data class Booking(
        val id: String = UUID.randomUUID().toString(),
        val providerName: String,
        val dateLabel: String,  // e.g., "Wed, Aug 6, 2025"
        val timeLabel: String,  // e.g., "02:00 PM"
        val createdAtMillis: Long = System.currentTimeMillis()
    )

    // Keep placed bookings here for “real-time” feel inside the app session
    private val _bookings = MutableStateFlow<List<Booking>>(emptyList())
    val bookings = _bookings.asStateFlow()

    fun addBooking(providerName: String, dateLabel: String, timeLabel: String) {
        _bookings.value = _bookings.value + Booking(
            providerName = providerName,
            dateLabel = dateLabel,
            timeLabel = timeLabel
        )
    }
}
