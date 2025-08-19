package com.example.laundry.screens

import android.content.Context
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
import androidx.compose.runtime.getValue
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
import androidx.core.content.ContentProviderCompat.requireContext
import coil.compose.AsyncImage
import com.project.common_utils.R
import com.project.common_utils.components.CircularImageHolderDrawable
import java.util.Locale

/* ───────────────────────── Data models ───────────────────────── */

data class OrderItem(
    val name: String,
    val unitPrice: Double,     // numeric price
    val unitSuffix: String,    // e.g., "/kg" or ""
    val qty: Int               // e.g., 5 meaning "x5"
)

data class Order(
    val providerName: String,
    val providerPhotoUrl: String,
    val category: String = "Laundry",
    val dateLabel: String,     // "20 March, Thu - 14h"
    val addressLine1: String,
    val addressLine2: String,
    val items: List<OrderItem>,
    val deliveryFee: Double = 0.0
)

/* ───────────────────────── Screen ───────────────────────── */

@Composable
fun OrderScreen(
    order: Order,
    onOpen: (String) -> Unit = {},
    onBack: () -> Unit = {},
    onCancel: () -> Unit = {},
    onRemoveItem: (OrderItem) -> Unit = {},
    onPlaceOrder: (() -> Unit)? = null
) {
    val context = LocalContext.current
    // Fallback action if caller didn't pass one
    val placeOrder = remember(onPlaceOrder, context) {
        onPlaceOrder ?: { Toast.makeText(context, "Order Placed", Toast.LENGTH_SHORT).show() }
    }
    val orange = Color(0xFFFF8800)
    val lightDivider = Color(0x11000000)

    val subtotal by remember(order) {
        val sum = order.items.sumOf { it.unitPrice * it.qty }
        androidx.compose.runtime.mutableStateOf(sum)
    }
    val total = subtotal + order.deliveryFee

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
                Button(
                    onClick = placeOrder,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = orange)
                ) { Text("Place order") }
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
        ) {
            // Top colored header
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
                        TextButton(onClick = onCancel) {
                            Text("Cancel", color = Color.White)
                        }
                    }

                    // Provider info + date + address
                    Column(Modifier.fillMaxWidth().padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularImageHolderDrawable(R.drawable.profiled,"Question")
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
                        Text(
                            order.dateLabel,
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color.White
                            )
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

            // Content card-ish section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                SectionHeader("Laundry")

                order.items.forEachIndexed { idx, item ->
                    OrderRow(
                        item = item,
                        accent = orange,
                        onRemove = { onRemoveItem(item) }
                    )
                    Divider(color = lightDivider)
                    if (idx == order.items.lastIndex) Spacer(Modifier.height(4.dp))
                }

                // Subtotal & Delivery
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Subtotal", style = MaterialTheme.typography.titleMedium)
                    Text(
                        formatUSD(subtotal),
                        style = MaterialTheme.typography.titleMedium,
                        color = orange
                    )
                }
                HorizontalDivider(color = lightDivider)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Delivery fees", style = MaterialTheme.typography.titleMedium, color = Color(0xFF9E9E9E))
                    Text(
                        formatUSD(order.deliveryFee),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF9E9E9E)
                    )
                }
                HorizontalDivider(color = lightDivider)
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

/* ───────────────────────── Pieces ───────────────────────── */

@Composable
private fun SectionHeader(title: String) {
    Text(
        title,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun OrderRow(
    item: OrderItem,
    accent: Color,
    onRemove: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(1f)) {
            Text(item.name, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            TextButton(
                onClick = onRemove,
                contentPadding = PaddingValues(0.dp)
            ) { Text("Remove", color = accent) }
        }

        Column(horizontalAlignment = Alignment.End) {
            // Unit price: "$ 15/kg"
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

/* ───────────────────────── Utils ───────────────────────── */

private fun formatUSD(value: Double): String {
    // Simple, locale-stable formatting like "$ 110.00"
    return "$ " + String.format(Locale.US, "%,.2f", value)
}

private fun Double.clean2(): String = String.format(Locale.US, "%,.0f", this)
    .let { if (this % 1.0 == 0.0) it else String.format(Locale.US, "%,.2f", this) }

/* ───────────────────────── Preview ───────────────────────── */

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun OrderScreenPreview() {
    val order = Order(
        providerName = "Jenny Jones",
        providerPhotoUrl = "https://images.pexels.com/photos/532220/pexels-photo-532220.jpeg",
        dateLabel = "20 March, Thu - 14h",
        addressLine1 = "28 Broad Street",
        addressLine2 = "Johannesburg",
        items = listOf(
            OrderItem("Cleaning", unitPrice = 15.0, unitSuffix = "/kg", qty = 5),
            OrderItem("Dry cleaning", unitPrice = 10.0, unitSuffix = "", qty = 2),
            OrderItem("Ironing", unitPrice = 3.0, unitSuffix = "/kg", qty = 5)
        ),
        deliveryFee = 0.0
    )
    MaterialTheme { OrderScreen(order) }
}
