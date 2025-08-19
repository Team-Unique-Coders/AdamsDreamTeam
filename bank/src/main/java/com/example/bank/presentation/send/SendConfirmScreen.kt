package com.example.bank.presentation.send

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
    val creditGreen = Color(0xFF10C971)

    val fees = 0.0
    val total = amount + fees
    val afterBalance = currentBalance - total

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Send money", fontWeight = FontWeight.SemiBold) },
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // Big amount on top
            Text(
                text = "$ ${"%,.2f".format(amount)}",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 48.sp
                ),
                color = brandOrange,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // Selected contact card
            SelectedContactCard(contact, brandOrange)

            // Transaction summary card
            TransactionSummaryCard(total, afterBalance, brandOrange, creditGreen)

            Spacer(Modifier.weight(1f))

            // Send button
            OrangeButton(
                onClick = onConfirmSend,
                text = "Send",
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SelectedContactCard(contact: Contact, brandOrange: Color) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFAF5)),
        elevation = CardDefaults.cardElevation(12.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .width(240.dp)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(color = Color(0xFFFFF2E6), shape = CircleShape)
                    .border(width = 2.dp, color = brandOrange, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    contact.initials,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 26.sp),
                    color = brandOrange
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                contact.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TransactionSummaryCard(total: Double, afterBalance: Double, brandOrange: Color, creditGreen: Color) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8F0)),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Total amount
            Text("Total amount", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Text(
                "$ ${"%,.2f".format(total)}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = brandOrange
            )

            // Fees
            Text("Transaction fees", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Text("$ 0.00", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

            // Balance after payment
            Text("Balance after payment", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Text(
                "$ ${"%,.2f".format(afterBalance)}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = creditGreen
            )
        }
    }
}
