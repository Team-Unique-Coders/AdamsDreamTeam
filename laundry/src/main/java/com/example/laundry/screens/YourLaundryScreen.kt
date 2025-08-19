package com.example.laundry.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.laundry.data.LaundryOptions
import com.example.laundry.data.Provider
import com.project.common_utils.components.OrangeButton
import com.project.common_utils.components.RangeSliderComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YourLaundryScreen(
    provider: Provider,
    selectedDate: String,
    selectedTime: String,
    onOpen: (String) -> Unit = {},                 // keep onOpen for navigation
    onBack: () -> Unit = {},
    onConfirmOptions: (LaundryOptions) -> Unit = {} // used to stash options then call onOpen(Order)
) {
    // Local UI state for the options
    var kgRange by remember { mutableStateOf(0f..10f) }        // endInclusive -> kg
    var drySelected by remember { mutableStateOf("1") }        // dropdown
    var ironing by remember { mutableStateOf(true) }           // yes/no
    var availability by remember { mutableStateOf(0f..10f) }   // range

    Scaffold(
        modifier = Modifier.background(Color.White),
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFFFF9800)
                        )
                    }
                },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = provider.photoUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(
                                text = provider.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "$selectedDate • $selectedTime",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OrangeButton(
                    onClick = {
                        val options = LaundryOptions(
                            kg = kgRange.endInclusive.toInt(),
                            dryCount = drySelected.toIntOrNull() ?: 1,
                            ironing = ironing,
                            availabilityMin = availability.start.toInt(),
                            availabilityMax = availability.endInclusive.toInt()
                        )
                        onConfirmOptions(options) // nav layer will save & call onOpen(ORDER route)
                    },
                   "Next"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // Laundry/kg – use end of range as the selected kg
            RangeSliderComponent(
                heading = "Laundry/kg",
                range = kgRange,
                onRangeChange = { kgRange = it },
                valueRange = 0f..50f,
                steps = 50
            )

            Spacer(Modifier.height(24.dp))

            Text("Dry", color = Color.Gray)
            Spacer(Modifier.height(8.dp))
            LargeDropdownMenu(
                options = listOf("1","2","3","4","5"),
                selectedOption = drySelected,
                onOptionSelected = { drySelected = it },
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(Modifier.height(24.dp))

            IroningChooser(
                value = ironing,
                onChange = { ironing = it }
            )

            Spacer(Modifier.height(24.dp))

            RangeSliderComponent(
                heading = "Availability",
                range = availability,
                onRangeChange = { availability = it },
                valueRange = 0f..50f,
                steps = 5
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun IroningChooser(
    value: Boolean,
    onChange: (Boolean) -> Unit
) {
    Text("Ironing", color = Color.Gray)
    Spacer(Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = value,
                onCheckedChange = { if (it) onChange(true) },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFFFF9800),
                    uncheckedColor = Color.Gray,
                    checkmarkColor = Color.White
                )
            )
            Spacer(Modifier.width(4.dp))
            Text("Yes")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = !value,
                onCheckedChange = { if (it) onChange(false) },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFFFF9800),
                    uncheckedColor = Color.Gray,
                    checkmarkColor = Color.White
                )
            )
            Spacer(Modifier.width(4.dp))
            Text("No")
        }
    }
}
