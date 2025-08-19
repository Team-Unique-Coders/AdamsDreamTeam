package com.example.handyman.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.common_utils.components.BackArrowIcon
import com.example.handyman.R as HmR

/* --- brand tokens --- */
private val Orange = Color(0xFFFF7A00)          // <- exact brand orange (matches header)
private val DividerGray = Color(0xFFEAECEF)

@Composable
fun OrderCheckoutScreen(
    onBack: () -> Unit,
    onPlaceOrder: () -> Unit,
    // pass real values if you have them
    title: String = "Order",
    service: String = "Plumber",
    pricePerHour: Int = 15,
    quantity: Int = 3,
    providerName: String = "Jenny Jones",
    dateLabel: String = "20 March, Thu • 14h",
    addressLine1: String = "28 Broad Street",
    addressLine2: String = "Johannesburg",
    @DrawableRes providerAvatar: Int = HmR.drawable.profilepicture
) {
    val subtotal = pricePerHour * quantity
    val delivery = 0
    val total = subtotal + delivery

    Scaffold(
        /* Bottom anchored CTA that avoids the gesture area and stays centered */
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onPlaceOrder,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Orange,       // <-- same orange as header
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    Text(
                        "Place order",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            /* ---------- Top bar ---------- */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackArrowIcon(onClick = onBack)
                Spacer(Modifier.width(12.dp))
                Text(title, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.weight(1f))
                Text("Cancel", color = Orange, fontSize = 14.sp)
            }

            /* ---------- Orange header card ---------- */
            Surface(
                color = Orange,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(providerAvatar),
                            contentDescription = providerName,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(18.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Handyman", color = Color.White.copy(alpha = 0.9f), fontSize = 13.sp)
                            Text(providerName, color = Color.White, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Column {
                        Text("Date", color = Color.White.copy(alpha = 0.9f))
                        Text(dateLabel, color = Color.White, fontWeight = FontWeight.Medium)
                    }

                    Column {
                        Text(addressLine1, color = Color.White, fontWeight = FontWeight.Medium)
                        Text(addressLine2, color = Color.White.copy(alpha = 0.9f))
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            /* ---------- White summary card ---------- */
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(service, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.weight(1f))
                        Text("$ $pricePerHour/h", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.width(6.dp))
                        Text("×$quantity")
                    }
                    Divider(color = DividerGray)

                    ValueRow(label = "Subtotal", value = "$ $subtotal")
                    ValueRow(label = "Delivery fees", value = "$ $delivery", valueAlpha = 0.5f)

                    Divider(color = DividerGray)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF7F8FA))
                            .padding(vertical = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Total amount", color = Color(0xFF8F9399))
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "$ $total.00",
                            color = Orange,                           // <-- matches button/header
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // leave space for content to scroll if needed;
            // the CTA sits in bottomBar, so no manual Spacer() is required here
        }
    }
}

@Composable
private fun ValueRow(
    label: String,
    value: String,
    valueAlpha: Float = 1f
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label)
        Spacer(Modifier.weight(1f))
        Text(value, color = Color.Black.copy(alpha = valueAlpha))
    }
}
