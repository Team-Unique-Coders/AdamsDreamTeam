// file: com/example/laundry/screens/OrderScreen.kt
package com.example.laundry.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.laundry.data.LaundryOptions
import com.example.laundry.data.LaundryViewModel
import com.example.laundry.navigation.LaundryDestinations
import com.project.common_utils.components.OrangeButton
import java.util.Locale

/* ───────────────────────── Data models ───────────────────────── */

data class OrderItem(
    val name: String,
    val unitPrice: Double,
    val unitSuffix: String,
    val qty: Int
)

data class Order(
    val providerName: String,
    val providerPhotoUrl: String,
    val category: String = "Laundry",
    val dateLabel: String,     // e.g., "2025-08-18 - 13:30"
    val addressLine1: String,
    val addressLine2: String,
    val items: List<OrderItem>,
    val deliveryFee: Double = 0.0,
    val options: LaundryOptions? = null,   // ✅ NEW
    val totalVal: Double = 0.0
)

/* ───────────────────────── Screen ───────────────────────── */

@Composable
fun OrderScreen(
    order: Order,
    onOpen: (String) -> Unit = {},
    onBack: () -> Unit = {},
    onCancel: () -> Unit = {},
    onRemoveItem: (OrderItem) -> Unit = {},
    onPlaceOrder: (() -> Unit)? = null,
    laundryViewModel: LaundryViewModel = viewModel()
) {
    val context = LocalContext.current
    val totalValue = null


    val orange = Color(0xFFFF8800)
    val lightDivider = Color(0x11000000)

    val subtotal = remember(order) { order.items.sumOf { it.unitPrice * it.qty } }
    val total = subtotal + order.deliveryFee
    val placeOrder = remember(onPlaceOrder, order, total, laundryViewModel) {
        onPlaceOrder ?: {
            val newOrder = order.copy(totalVal = total)
            laundryViewModel.addOrder(newOrder)           // <-- adds to shared VM
            Toast.makeText(context, "Order Placed", Toast.LENGTH_SHORT).show()
            onOpen(LaundryDestinations.HOME)       // or HOME
        }
    }

    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    "Total amount",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFB7B7B7),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    formatUSD(total),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = orange,
                        fontWeight = FontWeight.SemiBold
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(12.dp))
                OrangeButton(
                    onClick = placeOrder,
                    "Place order"
                )
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(orange)
            ) {
                Column(Modifier.fillMaxWidth()) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                        Text(
                            "Order",
                            modifier = Modifier.weight(1f),
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                        TextButton(onClick = onCancel) { Text("Cancel", color = Color.White) }
                    }

                    Column(Modifier.fillMaxWidth().padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = order.providerPhotoUrl,
                                contentDescription = "Provider",
                                modifier = Modifier.size(40.dp).clip(CircleShape)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(order.category, color = Color.White.copy(alpha = 0.9f))
                                Text(
                                    order.providerName,
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                        Text("Date", color = Color.White.copy(alpha = 0.9f))
                        Text(order.dateLabel, color = Color.White, style = MaterialTheme.typography.titleMedium)

                        Spacer(Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White)
                            Spacer(Modifier.width(6.dp))
                            Column {
                                Text(order.addressLine1, color = Color.White, style = MaterialTheme.typography.titleMedium)
                                Text(order.addressLine2, color = Color.White)
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }

            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                // ✅ Show selected options summary
                order.options?.let { opts ->
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)), // light orange
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Your selections", fontWeight = FontWeight.SemiBold, color = orange)
                            Spacer(Modifier.height(8.dp))
                            Text("Laundry/kg: ${opts.kg}")
                            Text("Dry count: ${opts.dryCount}")
                            Text("Ironing: ${if (opts.ironing) "Yes" else "No"}")
                            Text("Availability: ${opts.availabilityMin} – ${opts.availabilityMax}")
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }

                SectionHeader("Laundry")

                order.items.forEachIndexed { idx, item ->
                    OrderRow(
                        item = item,
                        accent = orange,
                        onRemove = { /* hook up if needed */ }
                    )
                    Divider(color = lightDivider)
                    if (idx == order.items.lastIndex) Spacer(Modifier.height(4.dp))
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Subtotal", style = MaterialTheme.typography.titleMedium)
                    Text(formatUSD(subtotal), style = MaterialTheme.typography.titleMedium, color = orange)
                }
                HorizontalDivider(color = lightDivider)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Delivery fees", style = MaterialTheme.typography.titleMedium, color = Color(0xFF9E9E9E))
                    Text(formatUSD(order.deliveryFee), style = MaterialTheme.typography.titleMedium, color = Color(0xFF9E9E9E))
                }
                HorizontalDivider(color = lightDivider)
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

/* ───────────────────────── Helpers & Preview ───────────────────────── */

@Composable private fun SectionHeader(title: String) {
    Text(
        title,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable private fun OrderRow(item: OrderItem, accent: Color, onRemove: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(1f)) {
            Text(item.name, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            TextButton(onClick = onRemove, contentPadding = PaddingValues(0.dp)) { Text("Remove", color = accent) }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(color = Color(0xFF4D4D4D))) { append("$ ") }
                    withStyle(SpanStyle(color = Color(0xFF4D4D4D), fontWeight = FontWeight.Medium)) {
                        append(item.unitPrice.clean2())
                    }
                    withStyle(SpanStyle(color = Color(0xFF4D4D4D))) { append(item.unitSuffix) }
                }
            )
            Spacer(Modifier.height(6.dp))
            Text("x${item.qty}", color = accent)
        }
    }
}

private fun formatUSD(value: Double) = "$ " + String.format(Locale.US, "%,.2f", value)
private fun Double.clean2(): String =
    if (this % 1.0 == 0.0) String.format(Locale.US, "%,.0f", this)
    else String.format(Locale.US, "%,.2f", this)

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun OrderScreenPreview() {
    val opts = LaundryOptions(
        kg = 5,
        dryCount = 2,
        ironing = true,
        availabilityMin = 9,
        availabilityMax = 17
    )
    val order = Order(
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
        options = opts
    )
    MaterialTheme { OrderScreen(order) }
}
