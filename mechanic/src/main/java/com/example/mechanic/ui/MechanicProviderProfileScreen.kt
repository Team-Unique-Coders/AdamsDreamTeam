package com.example.mechanic.ui

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.example.handyman.ui.ProviderProfileScreen
import com.example.mechanic.R as MeR

@Composable
fun MechanicProviderProfileScreen(
    onBack: () -> Unit,
    onTakeAppointment: () -> Unit,
    providerName: String = "Jenny Jones",
    @DrawableRes avatarRes: Int = MeR.drawable.profilepicture
) {
    ProviderProfileScreen(
        onBack = onBack,
        onTakeAppointment = onTakeAppointment,
        providerName = providerName,
        avatarRes = avatarRes
    )
}
