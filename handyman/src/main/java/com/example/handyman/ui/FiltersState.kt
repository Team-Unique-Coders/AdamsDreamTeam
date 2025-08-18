package com.example.handyman.ui

import java.io.Serializable

/**
 * Filters selected by the user on the Filters screen.
 * Marked Serializable so we can pass it via SavedStateHandle between destinations.
 */
data class FiltersState(
    val pricePerHourMax: Int = 100,      // e.g., cap at $100/h by default
    val minRating: Float = 0f,           // 0..5
    val maxDistanceMeters: Int = 2000,   // default 2km
    val categories: List<String> = emptyList() // e.g., ["Plumber", "Electrician"]
) : Serializable
