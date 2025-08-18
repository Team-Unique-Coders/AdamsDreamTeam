package com.example.laundry.data

data class Provider(
    val name: String,
    val rating: Double,
    val distanceMiles: Double,
    val priceText: String,
    val photoUrl: String,
    val lat: Double,
    val lon: Double,
    val address: String,
    val services: List<Pair<String, String>>,
    val about: String = "Friendly professional with fast turnaround and great quality."
)

val fake = listOf(
    Provider(
        "Jenny Jones", 4.8, 4.5, "$ 15/kg",
        "https://images.pexels.com/photos/532220/pexels-photo-532220.jpeg",
        -6.396, 106.823, "28 Broad Street, Johannesburg",
        listOf("Cleaning" to "$15/kg", "Dry cleaning" to "$10", "Ironing" to "$3/kg")
    ),
    Provider(
        "Sacha Down", 4.8, 4.5, "$ 13/kg",
        "https://images.pexels.com/photos/3765120/pexels-photo-3765120.jpeg",
        -6.394, 106.820, "15 Orchard Rd, Johannesburg",
        listOf("Cleaning" to "$13/kg", "Dry cleaning" to "$12", "Ironing" to "$3/kg")
    )
)