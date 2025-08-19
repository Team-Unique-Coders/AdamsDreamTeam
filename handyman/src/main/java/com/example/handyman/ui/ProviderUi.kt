package com.example.handyman.ui

data class ProviderUi(
    val id: String,
    val name: String,
    val role: String,
    val pricePerHour: Int,
    val rating: Float,
    val distanceMeters: Int
)
