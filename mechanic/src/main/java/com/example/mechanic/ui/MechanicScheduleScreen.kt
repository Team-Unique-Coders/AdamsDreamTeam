package com.example.mechanic.ui

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.example.handyman.ui.ScheduleScreen
import com.example.mechanic.R as MeR

@Composable
fun MechanicScheduleScreen(
    onBack: () -> Unit,
    onConfirm: (dateLabel: String, timeLabel: String) -> Unit,
    providerName: String = "Jenny Jones",
    @DrawableRes providerAvatar: Int = MeR.drawable.profilepicture
) {
    ScheduleScreen(
        onBack = onBack,
        onConfirm = onConfirm,
        providerName = providerName,
        providerAvatar = providerAvatar
    )
}
