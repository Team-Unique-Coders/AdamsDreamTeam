package com.example.bank.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.bank.domain.model.Account
import com.example.bank.domain.model.Transaction
import com.example.bank.domain.model.TxnType
import com.project.common_utils.components.OrangeButton
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankHomeScreen(
    accounts: List<Account> = emptyList(),
    transactions: List<Transaction> = emptyList(),
    onClose: (() -> Unit)? = null,
    onOpenChecking: (() -> Unit)? = null,
    onOpenTransaction: ((String) -> Unit)? = null
) {
    val brandOrange = Color(0xFFFF7A1A)
    val creditGreen = Color(0xFF10C971)

    var tab by remember { mutableStateOf(0) } // 0=All, 1=Sent, 2=Received
    var query by remember { mutableStateOf("") }
    val totalBalance = accounts.sumOf { it.balance }

    Scaffold(
        containerColor = Color.White, // ← full screen white
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.Black,
                    titleContentColor = Color.Black
                ),
                navigationIcon = {
                    onClose?.let {
                        IconButton(onClick = it) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: open filters */ }) {
                        Icon(Icons.Filled.Tune, contentDescription = "Filter")
                    }
                }
            )
        }
    ) { inner ->
        // Empty state → open Checking
        if (accounts.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Let’s open your first account", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Start with a Checking account to send and receive money.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(24.dp))
                OrangeButton(text = "Open Checking account", onClick = { onOpenChecking?.invoke() })
            }
            return@Scaffold
        }

        // Filter + search
        val filtered = remember(transactions, tab, query) {
            transactions
                .asSequence()
                .filter { t -> when (tab) { 1 -> t.type == TxnType.DEBIT; 2 -> t.type == TxnType.CREDIT; else -> true } }
                .filter { t -> query.isBlank() || t.counterparty.contains(query, ignoreCase = true) }
                .sortedByDescending { it.timestampMs }
                .toList()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
        ) {
            // Header: balance + avatar (on white)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text("Your balance", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(2.dp))
                    Text("$ ${"%,.2f".format(totalBalance)}", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                }
                Surface(tonalElevation = 0.dp, color = Color(0xFFF2F2F2), shape = CircleShape) {
                    Icon(Icons.Filled.Person, contentDescription = null, modifier = Modifier.size(40.dp).padding(6.dp))
                }
            }

            // Tabs — black labels + orange indicator on white background
            TabRow(
                selectedTabIndex = tab,
                containerColor = Color.White,
                indicator = { positions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(positions[tab]),
                        color = brandOrange
                    )
                }
            ) {
                Tab(selected = tab == 0, onClick = { tab = 0 }) { Text("All", color = Color.Black, modifier = Modifier.padding(16.dp)) }
                Tab(selected = tab == 1, onClick = { tab = 1 }) { Text("Sent", color = Color.Black, modifier = Modifier.padding(16.dp)) }
                Tab(selected = tab == 2, onClick = { tab = 2 }) { Text("Received", color = Color.Black, modifier = Modifier.padding(16.dp)) }
            }

            // Search
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search transaction", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = Color(0xFFE6E6E6),   // border color when focused
                    unfocusedIndicatorColor = Color(0xFFE6E6E6), // border color when not focused
                    disabledIndicatorColor = Color(0xFFE6E6E6),
                    cursorColor = Color.Black
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .fillMaxWidth()
            )



            // Group by day buckets
            val groups = remember(filtered) { groupByDay(filtered) }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (groups.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("No transactions yet", style = MaterialTheme.typography.titleMedium)
                            Text(
                                "Make your first payment or add money.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    groups.forEach { (header, items) ->
                        item { SectionHeader(header) }
                        items(items) { t ->
                            TransactionRow(
                                t,
                                creditGreen = creditGreen,
                                brandOrange = brandOrange,
                                onClick = { onOpenTransaction?.invoke(t.id) }   // ← calls back up
                            )
                        }
                        item { Spacer(Modifier.height(8.dp)) }
                    }
                    item { Spacer(Modifier.height(24.dp)) }
                }
            }
        }
    }
}

/* ---------- helpers ---------- */

private fun groupByDay(list: List<Transaction>): LinkedHashMap<String, List<Transaction>> {
    val result = LinkedHashMap<String, List<Transaction>>()
    val todayCal = Calendar.getInstance().apply { setToStartOfDay() }
    val yestCal = (todayCal.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -1) }
    val fmt = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())

    val grouped = list.groupBy { t ->
        val c = Calendar.getInstance().apply { timeInMillis = t.timestampMs; setToStartOfDay() }
        when {
            c.timeInMillis == todayCal.timeInMillis -> "Today"
            c.timeInMillis == yestCal.timeInMillis -> "Yesterday"
            else -> fmt.format(Date(t.timestampMs))
        }
    }.toSortedMap(compareBy<String> { key ->
        when (key) {
            "Today" -> Long.MAX_VALUE
            "Yesterday" -> Long.MAX_VALUE - 1
            else -> SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).parse(key)?.time ?: 0L
        }
    }).toList().asReversed()

    grouped.forEach { (k, v) -> result[k] = v }
    return result
}

private fun Calendar.setToStartOfDay() {
    set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
}

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
private fun TransactionRow(
    t: Transaction,
    creditGreen: Color,
    brandOrange: Color,
    onClick: (() -> Unit)? = null
) {
    ElevatedCard(
        onClick = { onClick?.invoke() },
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White) // ← pure white card
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(shape = CircleShape, tonalElevation = 0.dp, color = Color(0xFFF2F2F2)) {
                Icon(Icons.Filled.Person, contentDescription = null, modifier = Modifier.size(40.dp).padding(6.dp))
            }
            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                val prefix = if (t.type == TxnType.DEBIT) "to " else "from "
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                fontWeight = FontWeight.Normal
                            )
                        ) { append(prefix) }

                        withStyle(
                            SpanStyle(
                                color = Color.Black,
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) { append(t.counterparty) }
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                val date = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(t.timestampMs))
                Text(date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(Modifier.width(8.dp))

            val amountText = if (t.type == TxnType.CREDIT)
                "+ $ ${"%,.2f".format(t.amount)}"
            else
                "-$ ${"%,.2f".format(t.amount)}"

            val amountColor = if (t.type == TxnType.CREDIT) creditGreen else brandOrange

            Text(
                text = amountText,
                color = amountColor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
