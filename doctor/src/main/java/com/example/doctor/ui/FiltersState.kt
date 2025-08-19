package com.example.doctor.ui

import java.io.Serializable

data class FiltersState(
    val pricePerHourMax: Int = 100,      // e.g., cap at $100/h by default
    val minRating: Float = 0f,           // 0..5
    val maxDistanceMeters: Int = 2000,   // default 2km
    val categories: List<String> = emptyList() // e.g., ["Plumber", "Electrician"]
) : Serializable