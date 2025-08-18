package com.example.bank.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import com.example.bank.domain.model.Account

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankHomeScreen(
    accounts: List<Account> = emptyList(),
    onClose: (() -> Unit)? = null
) {
    var tab by remember { mutableStateOf(0) }
    var query by remember { mutableStateOf("") }

    val totalBalance = accounts.sumOf { it.balance } // Double from your model

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    if (onClose != null) {
                        IconButton(onClick = onClose) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: filters */ }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                    IconButton(onClick = { /* TODO: more */ }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
        ) {
            // Header: balance + avatar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        "Your balance",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "$ ${"%,.2f".format(totalBalance)}",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Surface(
                    tonalElevation = 2.dp,
                    shape = CircleShape
                ) {
                    // simple avatar placeholder
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(6.dp)
                    )
                }
            }

            // Tabs
            TabRow(selectedTabIndex = tab) {
                Tab(selected = tab == 0, onClick = { tab = 0 }) { Text("All", Modifier.padding(16.dp)) }
                Tab(selected = tab == 1, onClick = { tab = 1 }) { Text("Sent", Modifier.padding(16.dp)) }
                Tab(selected = tab == 2, onClick = { tab = 2 }) { Text("Received", Modifier.padding(16.dp)) }
            }

            // Search
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search transaction", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .fillMaxWidth()
            )

            // Sample groups: Today / Yesterday (static placeholders for now)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    SectionHeader("Today")
                }
                items(sampleToday) { item ->
                    TransactionCard(item)
                }

                item {
                    Spacer(Modifier.height(8.dp))
                    SectionHeader("Yesterday")
                }
                items(sampleYesterday) { item ->
                    TransactionCard(item)
                }

                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}

/* ---------- UI helpers (placeholder data + cards) ---------- */

private data class UiTxn(
    val id: String,
    val name: String,
    val date: String,
    val amount: String,   // formatted with sign e.g. "-$ 972.00" / "+$ 125.00"
    val isPositive: Boolean
)

private val sampleToday = listOf(
    UiTxn("t1", "Rebecca Moore", "20 January, 2019", "-$ 972.00", isPositive = false)
)

private val sampleYesterday = listOf(
    UiTxn("y1", "Franz Ferdinand", "19 January, 2019", "+$ 125.00", isPositive = true),
    UiTxn("y2", "Franz Ferdinand", "19 January, 2019", "+$ 125.00", isPositive = true),
    UiTxn("y3", "iclickigo",        "19 January, 2019", "-$ 9.50",  isPositive = false),
)

@Composable
private fun SectionHeader(title: String) {
    Text(
        title,
        modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 4.dp),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun TransactionCard(item: UiTxn) {
    ElevatedCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // avatar placeholder
            Surface(shape = CircleShape, tonalElevation = 2.dp) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(6.dp)
                )
            }
            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text("to: ${item.name}", style = MaterialTheme.typography.bodyLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(item.date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(Modifier.width(8.dp))

            Text(
                text = item.amount,
                color = if (item.isPositive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.width(8.dp))

            // tiny action button placeholder (square orange in the mock)
            FilledTonalButton(
                onClick = { /* TODO: quick action */ },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.size(width = 46.dp, height = 38.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("↗")
            }
        }
    }
}
