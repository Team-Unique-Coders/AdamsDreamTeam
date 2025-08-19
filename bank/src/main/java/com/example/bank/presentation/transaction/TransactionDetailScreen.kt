package com.example.bank.presentation.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bank.domain.model.Transaction
import com.example.bank.domain.model.TxnType
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    txn: Transaction,
    onBack: () -> Unit
) {
    val brandOrange = Color(0xFFFF7A1A)
    val creditGreen = Color(0xFF10C971)

    val dateStr = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
        .format(Date(txn.timestampMs))

    val isCredit = txn.type == TxnType.CREDIT
    val amountText = (if (isCredit) "+ $ " else "-$ ") + "%,.2f".format(txn.amount)
    val amountColor = if (isCredit) creditGreen else brandOrange

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Transaction", fontWeight = FontWeight.SemiBold) },
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
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // Date
            Text(
                dateStr,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(24.dp))

            // Merchant icon
            Surface(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally),
                shape = CircleShape,
                color = Color(0xFFF2F2F2)
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(18.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(24.dp))

            // Merchant name
            Text(
                txn.counterparty,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            // Info card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoRow("Number", "7505483498654-45-20")
                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE0E0E0))
                    InfoRow("VAT", "SE483498342897601")
                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE0E0E0))
                    InfoRow("Distributor", "iclickipay")
                }
            }

            Spacer(Modifier.height(32.dp))

            // Transaction summary card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Transaction fees", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    Spacer(Modifier.height(4.dp))
                    Text("$ 0", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

                    Spacer(Modifier.height(12.dp))
                    Text("Total amount", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        amountText,
                        color = amountColor,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
    }
}
