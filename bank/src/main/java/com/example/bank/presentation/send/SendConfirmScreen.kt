package com.example.bank.presentation.send

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bank.domain.model.Contact
import com.project.common_utils.components.OrangeButton
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendConfirmScreen(
    contact: Contact,
    amount: Double,
    currentBalance: Double,
    onBack: () -> Unit,
    onConfirmSend: () -> Unit
) {
    val brandOrange = Color(0xFFFF7A1A)
    val money = NumberFormat.getCurrencyInstance(Locale.US).apply { currency = java.util.Currency.getInstance("USD") }

    val fees = 0.0
    val total = amount + fees
    val afterBalance = currentBalance - total

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Send money") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    navigationIconContentColor = Color.Black,
                    titleContentColor = Color.Black
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))

            // Big amount on top
            Text(
                text = "$ ${"%,.2f".format(amount)}",
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))

            // Selected contact tile
            SelectedContactCard(contact = contact)

            Spacer(Modifier.height(24.dp))

            // Fees
            Text(
                "Transaction fees",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "$ 0",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(14.dp))

            // Total amount
            Text(
                "Total amount",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "$ ${"%,.2f".format(total)}",
                color = brandOrange,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))
            Divider(color = Color(0xFFEAEAEA))
            Spacer(Modifier.height(16.dp))

            // Balance after payment
            Text(
                "Your balance after payment",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "$ ${"%,.2f".format(afterBalance)}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.weight(1f))

            OrangeButton(
                onClick = onConfirmSend,
                text = "Send"
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

/** Simple centered card showing the chosen contact (initials avatar + name). */
@Composable
private fun SelectedContactCard(contact: Contact) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .width(220.dp)
                .padding(vertical = 16.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Placeholder avatar with initials
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(color = Color(0xFFECECEC), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contact.initials,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
                    color = Color.Black
                )
            }

            Spacer(Modifier.height(10.dp))
            Text(
                text = contact.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}
