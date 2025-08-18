package com.example.handyman.domain.model

data class ServiceProvider(
    val id: String,
    val name: String,
    val skill: String,
    val rating: Double,
    val pricePerHour: Int
)
