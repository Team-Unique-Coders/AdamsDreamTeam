// file: com/example/laundry/screens/ViewOrderScreen.kt
package com.example.laundry.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.laundry.data.LaundryViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewOrderScreen(
    orders: List<Order>,
    vm: LaundryViewModel = viewModel(),
    onOpen: (String) -> Unit = {},      // e.g., navigate to an order detail route if you add one
    onBack: () -> Unit = {},            // go back
    onRemove: (Order) -> Unit = { vm.removeOrder(it) }
) {
//    val orders by vm.orders.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFFFF9800)
                        )
                    }
                },
                title = { Text("Your Orders", fontWeight = FontWeight.SemiBold) }
            )
        }
    ) { inner ->
        if (orders.isEmpty()) {
            EmptyOrdersState(modifier = Modifier
                .fillMaxSize()
                .padding(inner))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(orders) { order ->
                    OrderListItem(
                        order = order,
                        onClick = { onOpen(/* add a route if you want to open details */ "") },
                        onDelete = { onRemove(order) }
                    )
                }
                item { Spacer(Modifier.height(12.dp)) }
            }
        }
    }
}

@Composable
private fun EmptyOrdersState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No orders yet", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text(
            "Your future orders will appear here.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
private fun OrderListItem(
    order: Order,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val orange = Color(0xFFFF9800)
    val subtotal = order.items.sumOf { it.unitPrice * it.qty }
    val total = (if (order.totalVal > 0.0) order.totalVal else subtotal + order.deliveryFee)

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = order.providerPhotoUrl,
                contentDescription = order.providerName,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    order.providerName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    order.dateLabel, // already "YYYY-MM-DD - HH:mm" per your flow
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 1
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    formatUSD(total),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = orange
                )
            }
            Spacer(Modifier.width(8.dp))
            TextButton(onClick = onDelete) { Text("Remove", color = orange) }
        }
    }
}

/* ───────────── Helpers ───────────── */

private fun formatUSD(value: Double): String =
    "$ " + String.format(Locale.US, "%,.2f", value)

/* ───────────── Preview (static data) ───────────── */

@Preview(showBackground = true)
@Composable
private fun OrderListItemPreview() {
    val sample = Order(
        providerName = "Jenny Jones",
        providerPhotoUrl = "https://images.pexels.com/photos/532220/pexels-photo-532220.jpeg",
        dateLabel = "2025-08-18 - 13:30",
        addressLine1 = "28 Broad Street",
        addressLine2 = "Johannesburg",
        items = listOf(
            OrderItem("Cleaning", 15.0, "/kg", 5),
            OrderItem("Dry cleaning", 10.0, "", 2),
            OrderItem("Ironing", 3.0, "/kg", 5)
        ),
        deliveryFee = 0.0,
        totalVal = 0.0
    )
    MaterialTheme {
        OrderListItem(order = sample, onClick = {}, onDelete = {})
    }
}
