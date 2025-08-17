package com.example.handyman.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.common_utils.BackArrowIcon
import com.project.common_utils.OrangeButton

@Composable
fun OrderCheckoutScreen(
    onBack: () -> Unit,
    onPlaceOrder: () -> Unit
) {
    // dummy values (match your mock)
    val providerName = "Jenny Jones"
    val role = "Handyman"
    val pricePerHour = 15
    val qtyHours = 3
    val dateText = "20 March, Thu • 14h"
    val address = "28 Broad Street\nJohannesburg"

    val subtotal = pricePerHour * qtyHours
    val delivery = 0
    val total = subtotal + delivery

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // top bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            BackArrowIcon(onClick = onBack)
            Spacer(Modifier.width(12.dp))
            Text("Order", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(Modifier.height(16.dp))

        // orange header card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(role, color = MaterialTheme.colorScheme.onPrimary)
                Text(providerName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary)
                Spacer(Modifier.height(12.dp))
                Text("Date", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f))
                Text(dateText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary)
                Spacer(Modifier.height(12.dp))
                Text(address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary)
            }
        }

        Spacer(Modifier.height(16.dp))

        // line items
        ElevatedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Plumber")
                    Text("$ $pricePerHour/h  ×$qtyHours")
                }

                Divider(Modifier.padding(vertical = 12.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Subtotal")
                    Text("$ ${subtotal}")
                }
                Spacer(Modifier.height(6.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Delivery fees")
                    Text("$ ${delivery}.0")
                }

                Divider(Modifier.padding(vertical = 12.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        "Total amount",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        "$ ${total}.0",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))

        // CTA
        OrangeButton(
            onClick = onPlaceOrder,
            text = "Place order"
        )
    }
}
