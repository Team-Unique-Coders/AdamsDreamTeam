package com.example.mechanic.ui

import androidx.compose.runtime.Composable
import com.example.handyman.ui.OrderCheckoutScreen

@Composable
fun MechanicOrderCheckoutScreen(
    onBack: () -> Unit,
    onPlaceOrder: () -> Unit,
    providerName: String,
    pricePerHour: Int,
    dateLabel: String
) {
    OrderCheckoutScreen(
        onBack = onBack,
        onPlaceOrder = onPlaceOrder,
        title = "Order",
        service = "Mechanic",           // label per mock
        providerName = providerName,
        pricePerHour = pricePerHour,
        dateLabel = dateLabel
    )
}
