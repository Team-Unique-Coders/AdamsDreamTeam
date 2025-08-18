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

/** Optional stable key if you need an ID for navigation */
fun providerKey(p: Provider): String =
    "%s|%.5f|%.5f".format(java.util.Locale.US, p.name, p.lat, p.lon)

val fake: List<Provider> = listOf(
    Provider(
        "Jenny Jones", 4.8, 4.5, "$ 15/kg",
        "https://images.pexels.com/photos/532220/pexels-photo-532220.jpeg",
        -6.396, 106.823, "28 Broad Street, Johannesburg",
        listOf("Cleaning" to "$15/kg", "Dry cleaning" to "$10", "Ironing" to "$3/kg")
    ),
    Provider(
        "Sacha Down", 4.7, 3.9, "$ 13/kg",
        "https://images.pexels.com/photos/3765120/pexels-photo-3765120.jpeg",
        -6.394, 106.820, "15 Orchard Rd, Johannesburg",
        listOf("Cleaning" to "$13/kg", "Dry cleaning" to "$12", "Ironing" to "$3/kg")
    ),
    Provider(
        "Clean & Care Co.", 4.6, 2.2, "$ 12/kg",
        "https://images.pexels.com/photos/3735612/pexels-photo-3735612.jpeg",
        -6.398, 106.826, "12 Victoria Ave, Johannesburg",
        listOf("Cleaning" to "$12/kg", "Dry cleaning" to "$11", "Ironing" to "$2/kg")
    ),
    Provider(
        "Sparkle Laundry", 4.9, 1.8, "$ 16/kg",
        "https://images.pexels.com/photos/3768146/pexels-photo-3768146.jpeg",
        -6.400, 106.824, "9 Kingsway, Johannesburg",
        listOf("Cleaning" to "$16/kg", "Dry cleaning" to "$13", "Ironing" to "$4/kg")
    ),
    Provider(
        "Pressed & Fresh", 4.5, 5.1, "$ 11/kg",
        "https://images.pexels.com/photos/4621479/pexels-photo-4621479.jpeg",
        -6.392, 106.818, "34 Melrose Rd, Johannesburg",
        listOf("Cleaning" to "$11/kg", "Dry cleaning" to "$9", "Ironing" to "$2/kg")
    ),
    Provider(
        "EcoWash Hub", 4.4, 6.4, "$ 10/kg",
        "https://images.pexels.com/photos/9797308/pexels-photo-9797308.jpeg",
        -6.389, 106.830, "5 Oak Street, Johannesburg",
        listOf("Cleaning" to "$10/kg", "Dry cleaning" to "$9", "Ironing" to "$2/kg"),
        about = "Eco-friendly detergents and water-saving machines."
    ),
    Provider(
        "City Quick Clean", 4.3, 3.2, "$ 14/kg",
        "https://images.pexels.com/photos/3965545/pexels-photo-3965545.jpeg",
        -6.401, 106.829, "88 Market Lane, Johannesburg",
        listOf("Cleaning" to "$14/kg", "Dry cleaning" to "$12", "Ironing" to "$3/kg")
    ),
    Provider(
        "Laundry Lounge", 4.8, 2.7, "$ 15/kg",
        "https://images.pexels.com/photos/3865713/pexels-photo-3865713.jpeg",
        -6.395, 106.832, "21 Baker Street, Johannesburg",
        listOf("Cleaning" to "$15/kg", "Dry cleaning" to "$11", "Ironing" to "$3/kg"),
        about = "Coffee, Wi-Fi, and comfy seats while you wait."
    ),
    Provider(
        "Bright Whites", 4.2, 7.0, "$ 9/kg",
        "https://images.pexels.com/photos/8952536/pexels-photo-8952536.jpeg",
        -6.387, 106.817, "3 Cedar Park, Johannesburg",
        listOf("Cleaning" to "$9/kg", "Dry cleaning" to "$8", "Ironing" to "$2/kg")
    ),
    Provider(
        "Golden Hangers", 4.6, 4.0, "$ 13/kg",
        "https://images.pexels.com/photos/3769747/pexels-photo-3769747.jpeg",
        -6.393, 106.828, "19 Newlands Ave, Johannesburg",
        listOf("Cleaning" to "$13/kg", "Dry cleaning" to "$12", "Ironing" to "$3/kg")
    )
)