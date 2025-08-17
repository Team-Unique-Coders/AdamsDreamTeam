package com.example.mechanic.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.common_utils.components.BackArrowIcon
import com.project.common_utils.components.OrangeButton

@Composable
fun MechanicOrderCheckoutScreen(
    onBack: () -> Unit,
    onPlaceOrder: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row {
            BackArrowIcon(onClick = onBack)
            Spacer(Modifier.width(12.dp))
            Text("Order", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(Modifier.height(16.dp))

        Card {
            Column(Modifier.fillMaxWidth().padding(16.dp)) {
                Text("Mechanic  •  $15/h")
                Spacer(Modifier.height(8.dp))
                Text("Date: 20 Mar, Thu – 14h")
                Text("Address: 28 Broad Street, Johannesburg")
                Divider(Modifier.padding(vertical = 12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total amount")
                    Text("$45.00", style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        Spacer(Modifier.weight(1f))
        OrangeButton(onClick = onPlaceOrder, text = "Place order")
    }
}
