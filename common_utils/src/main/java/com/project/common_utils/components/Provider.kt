package com.project.common_utils.components

import androidx.annotation.DrawableRes
import com.project.common_utils.IconTextModel
import com.project.common_utils.R
import com.project.common_utils.TextSpacerTextModel

data class Provider(
    val name: String,
    val rating: Double,
    val distanceMiles: Double,
    val priceText: String,
    @DrawableRes val photoUrl: Int,
    val lat: Double,
    val lon: Double,
    val address: String,
    val textSpacerTextModelList: List<TextSpacerTextModel> = listOf(),
    val iconTextModelList: List<IconTextModel> = listOf(),
    val about: String = "Friendly professional with fast turnaround and great quality."
)

val fake = listOf(
    Provider(
        "Jenny Jones", 4.8, 4.5, "$ 15/kg",
        R.drawable.profiled,
        -6.396, 106.823, "28 Broad Street, Johannesburg"
    ),
    Provider(
        "Sacha Down", 4.8, 4.5, "$ 13/kg",
        R.drawable.profiled,
        -6.394, 106.820, "15 Orchard Rd, Johannesburg"
    )
)

val fakeLaundry = listOf(
    Provider(
        "Jenny Jones", 4.8, 4.5, "$ 15/kg",
        R.drawable.profiled,
        -6.396, 106.823, "28 Broad Street, Johannesburg",
        listOf(
            TextSpacerTextModel("Cleaning", "$15/kg"),
            TextSpacerTextModel("Dry cleaning", "$10"),
            TextSpacerTextModel("Ironing", "$3/kg")
        ),

        ),
    Provider(
        "Sacha Down", 4.8, 4.5, "$ 13/kg",
        R.drawable.profiled,
        -6.394, 106.820, "15 Orchard Rd, Johannesburg",
        listOf(
            TextSpacerTextModel("Cleaning", "$13/kg"),
            TextSpacerTextModel("Dry cleaning", "$12"),
            TextSpacerTextModel("Ironing", "$3/kg")
        ),
    )
)

val fakeLearning = listOf(
    Provider(
        "Jenny Jones", 4.8, 4.5, "$ 15/kg",
        R.drawable.profiled,
        -6.396, 106.823, "28 Broad Street, Johannesburg",
        iconTextModelList = listOf(
            IconTextModel("📚", listOf("English", "French")),
            IconTextModel("📊", listOf("Elementary", "College"))
        )
    ),
    Provider(
        "Sacha Down", 4.8, 4.5, "$ 13/kg",
        R.drawable.profiled,
        -6.394, 106.820, "15 Orchard Rd, Johannesburg",
        iconTextModelList = listOf(
            IconTextModel("📚", listOf("Math")),
            IconTextModel("📊", listOf("High School"))
        )
    )
)

val fakeHandyman = listOf(
    Provider(
        "Jenny Jones", 4.8, 4.5, "$ 15/kg",
        R.drawable.profiled,
        -6.396, 106.823, "28 Broad Street, Johannesburg",
        iconTextModelList = listOf(
            IconTextModel("🔧", listOf("Plumber", "Carpenter")),
        )
    ),
    Provider(
        "Sacha Down", 4.8, 4.5, "$ 13/kg",
        R.drawable.profiled,
        -6.394, 106.820, "15 Orchard Rd, Johannesburg",
        iconTextModelList = listOf(
            IconTextModel("🔧", listOf("Electrician"))
        )
    )
)

val fakeMechanic = listOf(
    Provider(
        "Jenny Jones", 4.8, 4.5, "$ 15/kg",
        R.drawable.profiled,
        -6.396, 106.823, "28 Broad Street, Johannesburg",
        iconTextModelList = listOf(
            IconTextModel("⚙️", listOf("Car", "Motorcycle"))
        )
    ),
    Provider(
        "Sacha Down", 4.8, 4.5, "$ 13/kg",
        R.drawable.profiled,
        -6.394, 106.820, "15 Orchard Rd, Johannesburg",
        iconTextModelList = listOf(
            IconTextModel("⚙️", listOf("Truck"))
        )
    )
)


