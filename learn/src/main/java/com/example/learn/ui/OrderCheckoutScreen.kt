package com.example.learn.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.common_utils.components.BackArrowIcon
import com.project.common_utils.components.OrangeButton

@Composable
fun LearnOrderCheckoutScreen(
    onBack: () -> Unit,
    onPlaceOrder: () -> Unit
){
    val providerName = "Jenny Jones"
    val role = "Tutor"
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
                    Text("English")
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