package com.example.laundry.data

// file: com/example/laundry/model/LaundryOptions.kt

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LaundryOptions(
    val kg: Int,                 // chosen laundry kg (take end of slider)
    val dryCount: Int,           // dropdown value
    val ironing: Boolean,        // Yes/No
    val availabilityMin: Int,    // availability start
    val availabilityMax: Int     // availability end
) : Parcelable
