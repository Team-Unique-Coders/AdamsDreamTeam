// file: com/example/bank/presentation/filters/FiltersScreen.kt
package com.example.bank.presentation.filters

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.common_utils.components.OrangeButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersScreen(
    initialMonthsRange: IntRange = 6..12,                 // 12 = “Now”
    initialCategories: Set<String> = emptySet(),
    onApply: (monthsRange: IntRange, categories: Set<String>) -> Unit,
    onClear: () -> Unit,
    onBack: () -> Unit,
    @DrawableRes illustrationRes: Int = com.example.bank.R.drawable.bankintro
) {
    val brandOrange = Color(0xFFFF7A1A)

    // Independent state (fixes “checkbox updates only after moving slider”)
    var start by remember { mutableIntStateOf(initialMonthsRange.first) }
    var end by remember { mutableIntStateOf(initialMonthsRange.last) }

    // Selection that recomposes immediately
    val allTags = listOf(
        "Cleaning","Delivery","Dog","Food",
        "Friends","Hotel","Lavery","Love",
        "Services","Strip","Transport","Tutor","Rent house"
    )
    val selected = remember {
        mutableStateListOf<String>().apply { addAll(initialCategories) }
    }
    fun toggle(tag: String) {
        if (selected.contains(tag)) selected.remove(tag) else selected.add(tag)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filters") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color(0xFFFF7A1A)
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = onClear) {
                        Text("Clear", color = brandOrange, fontWeight = FontWeight.SemiBold)
                    }
                }
            )
        },
        containerColor = Color.White
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // (Optional) header illustration
            Image(
                painter = painterResource(illustrationRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(bottom = 4.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Date / Year range
            Text("Date", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Divider(Modifier.padding(vertical = 4.dp))

            Text("Year", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(6.dp))

            // 0..12 months, where 12 == Now
            var slider by remember { mutableStateOf(start.toFloat()..end.toFloat()) }
            RangeSlider(
                value = slider,
                onValueChange = { range ->
                    slider = range
                    start = range.start.toInt().coerceIn(0, 12)
                    end = range.endInclusive.toInt().coerceIn(0, 12)
                },
                valueRange = 0f..12f,
                steps = 11,
                colors = SliderDefaults.colors(
                    thumbColor = brandOrange,
                    activeTrackColor = brandOrange
                )
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(if (start == 0) "Now" else "${start} mo", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(if (end == 12) "Now" else "${end} mo", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(Modifier.height(16.dp))

            // Services
            Text("Services", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Divider(Modifier.padding(vertical = 4.dp))

            // Simple 2-column grid of checkboxes
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                allTags.chunked(2).forEach { row ->
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        row.forEach { tag ->
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .toggleable(
                                        value = selected.contains(tag),
                                        onValueChange = { toggle(tag) }
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = selected.contains(tag),
                                    onCheckedChange = { toggle(tag) },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = brandOrange,
                                        uncheckedColor = MaterialTheme.colorScheme.outline
                                    )
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(tag)
                            }
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }

            Spacer(Modifier.weight(1f))
            OrangeButton(
                onClick = { onApply(start..end, selected.toSet()) },
                text = "Apply"
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}
