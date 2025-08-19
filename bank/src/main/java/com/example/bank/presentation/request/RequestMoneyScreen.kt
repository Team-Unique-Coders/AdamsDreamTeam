package com.example.bank.presentation.request

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.bank.domain.model.Contact

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestMoneyScreen(
    contacts: List<Contact>,
    recents: List<Contact> = emptyList(),
    presetContactId: String? = null,
    onBack: () -> Unit,
    onShowAllContacts: () -> Unit = {},
    onChangeCurrency: () -> Unit = {},
    onNext: (contactId: String, amount: Double, description: String) -> Unit
) {
    val brandOrange = Color(0xFFFF7A1A)
    val selectedBlue = Color(0xFF1E4BFF)

    var query by rememberSaveable { mutableStateOf("") }
    var selectedContactId by rememberSaveable { mutableStateOf(presetContactId) }
    var amountText by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    var contactErr by rememberSaveable { mutableStateOf<String?>(null) }
    var amountErr by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(presetContactId) {
        contacts.firstOrNull { it.id == presetContactId }?.let { query = it.name }
    }
    LaunchedEffect(query) {
        val match = contacts.firstOrNull {
            it.name.equals(query, true) ||
                    it.email?.equals(query, true) == true ||
                    it.phone?.equals(query, true) == true
        }
        if (match != null) selectedContactId = match.id
    }

    fun parseAmount(): Double? = amountText.replace(",", "").toDoubleOrNull()?.takeIf { it > 0.0 }
    fun proceed() {
        val amount = parseAmount()
        val chosenId = selectedContactId ?: contacts.firstOrNull {
            it.name.equals(query, true) ||
                    it.email?.equals(query, true) == true ||
                    it.phone?.equals(query, true) == true
        }?.id

        contactErr = if (chosenId == null) "Choose a contact" else null
        amountErr = if (amount == null) "Enter a valid amount" else null
        if (chosenId != null && amount != null) onNext(chosenId, amount, description.trim())
    }

    val suggestions = remember(query, contacts) {
        val q = query.trim()
        if (q.isBlank()) emptyList()
        else contacts.filter {
            it.name.contains(q, true) ||
                    (it.email?.contains(q, true) == true) ||
                    (it.phone?.contains(q, true) == true)
        }.take(6)
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Ask money", fontWeight = FontWeight.SemiBold) },
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
        },
        bottomBar = {
            Button(
                onClick = { proceed() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .height(56.dp)
                    .shadow(8.dp, shape = RoundedCornerShape(6.dp)),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = brandOrange,
                    contentColor = Color.White
                ),
                enabled = (selectedContactId != null && parseAmount() != null)
            ) { Text("Continue", fontWeight = FontWeight.SemiBold) }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            Text("Enter contact", style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = query,
                onValueChange = { query = it; contactErr = null },
                placeholder = { Text("Name, email or phone number",
                    color = MaterialTheme.colorScheme.onSurfaceVariant) },
                singleLine = true,
                isError = contactErr != null,
                supportingText = { contactErr?.let { Text(it) } },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = selectedBlue,
                    unfocusedIndicatorColor = Color(0xFFE6E6E6),
                    disabledIndicatorColor = Color(0xFFE6E6E6),
                    cursorColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Search
                ),
                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)
            )

            if (suggestions.isNotEmpty()) {
                Spacer(Modifier.height(6.dp))
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
                ) {
                    LazyColumn(Modifier.heightIn(max = 220.dp),
                        contentPadding = PaddingValues(vertical = 4.dp)) {
                        items(suggestions, key = { it.id }) { c ->
                            SuggestionRow(c) {
                                query = c.name
                                selectedContactId = c.id
                                contactErr = null
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            Row(Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text("Debtor", style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                TextButton(onClick = onShowAllContacts, contentPadding = PaddingValues(0.dp)) {
                    Text("Show all")
                }
            }

            val carousel = if (recents.isNotEmpty()) recents else contacts.take(10)
            Spacer(Modifier.height(6.dp))
            LazyRow(contentPadding = PaddingValues(horizontal = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(carousel, key = { it.id }) { c ->
                    ContactTile(c, c.id == selectedContactId, selectedBlue) {
                        selectedContactId = c.id
                        query = c.name
                        contactErr = null
                    }
                }
            }

            Spacer(Modifier.height(22.dp))

            Text("Transaction details", style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))

            Text("Amount", style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = amountText,
                onValueChange = {
                    val filtered = it.filter { ch -> ch.isDigit() || ch == '.' || ch == ',' }
                    val parts = filtered.split('.', limit = 2)
                    amountText = if (parts.size == 2)
                        parts[0].replace(",", "") + "." + parts[1].replace(".", "").replace(",", "")
                    else filtered.replace(",", "")
                    amountErr = null
                },
                singleLine = true,
                leadingIcon = { Text("$", fontSize = 18.sp, modifier = Modifier.padding(start = 4.dp)) },
                trailingIcon = { TextButton(onClick = onChangeCurrency) { Text("change currency") } },
                isError = amountErr != null,
                supportingText = { amountErr?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = selectedBlue,
                    unfocusedIndicatorColor = Color(0xFFE6E6E6),
                    disabledIndicatorColor = Color(0xFFE6E6E6),
                    cursorColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)
            )

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Description (optional)",
                    color = MaterialTheme.colorScheme.onSurfaceVariant) },
                singleLine = false, minLines = 2,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = Color(0xFFE6E6E6),
                    unfocusedIndicatorColor = Color(0xFFE6E6E6),
                    disabledIndicatorColor = Color(0xFFE6E6E6),
                    cursorColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun SuggestionRow(contact: Contact, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable(onClick = onClick)
        .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(34.dp).background(Color(0xFFEFF1F6), CircleShape),
            contentAlignment = Alignment.Center) {
            Text(contact.initials, fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(contact.name, fontWeight = FontWeight.Medium)
            val sub = contact.email ?: contact.phone ?: ""
            if (sub.isNotBlank())
                Text(sub, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ContactTile(
    contact: Contact,
    selected: Boolean,
    selectedBlue: Color,
    onClick: () -> Unit
) {
    val bg = if (selected) selectedBlue else Color(0xFFF2F4F7)
    val title = if (selected) Color.White else Color(0xFF1B1B1B)
    val subtitle = if (selected) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = Modifier
            .width(180.dp).height(150.dp)
            .shadow(if (selected) 12.dp else 2.dp, RoundedCornerShape(16.dp))
            .background(bg, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(Modifier.size(48.dp)
            .background(if (selected) Color.White.copy(alpha = 0.18f) else Color.White, CircleShape),
            contentAlignment = Alignment.Center) {
            Text(contact.initials,
                color = if (selected) Color.White else selectedBlue,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold)
        }
        val parts = contact.name.trim().split(Regex("\\s+"))
        val line1 = parts.firstOrNull().orElse("")
        val line2 = parts.drop(1).joinToString(" ").take(18)
        Text(line1, color = title, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
        Text(line2, color = subtitle, style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center, lineHeight = 14.sp)
    }
}

private fun String?.orElse(fallback: String) = this ?: fallback
