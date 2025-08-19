package com.example.mechanic.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.project.common_utils.components.BackArrowIcon

private val Orange = Color(0xFFFF7A00)
private val DividerGrey = Color(0xFFE7E8EB)
private val LabelGrey = Color(0xFF9AA0A6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MechanicFormScreen(
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    var type by remember { mutableStateOf("Car") }
    var model by remember { mutableStateOf("Lexus") }
    var year by remember { mutableStateOf("2016") }
    var motor by remember { mutableStateOf("Gas") }

    // Availability: 8/11/14/17/20h
    val hours = listOf(8, 11, 14, 17, 20)
    var availabilityIndex by remember { mutableIntStateOf(2) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackArrowIcon(onClick = onBack, tint = Orange)
            Spacer(Modifier.width(12.dp))
            Text("Your mechanic", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(Modifier.height(12.dp))

        LabeledDropdown("Type", listOf("Car","Motorcycle","Truck"), type, { type = it })
        Spacer(Modifier.height(12.dp))

        LabeledDropdown("Model", listOf("Lexus","BMW","Audi","Honda","Toyota"), model, { model = it })
        Spacer(Modifier.height(12.dp))

        LabeledDropdown("Year", (2008..2025).map(Int::toString), year, { year = it })
        Spacer(Modifier.height(12.dp))

        LabeledDropdown("Motor", listOf("Gas","Diesel","Hybrid","Electric"), motor, { motor = it })

        Spacer(Modifier.height(22.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Availability", color = LabelGrey, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.width(10.dp))
            Box(
                Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(DividerGrey)
            )
        }

        Spacer(Modifier.height(10.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            hours.forEachIndexed { i, h ->
                Text(
                    "${h}h",
                    color = if (i == availabilityIndex) Color(0xFF30313A) else LabelGrey,
                    style = if (i == availabilityIndex)
                        MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall
                )
            }
        }

        Slider(
            value = availabilityIndex.toFloat(),
            onValueChange = { availabilityIndex = it.toInt().coerceIn(0, hours.lastIndex) },
            valueRange = 0f..hours.lastIndex.toFloat(),
            steps = hours.size - 2,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = Orange,
                activeTrackColor = Orange,
                inactiveTrackColor = DividerGrey
            )
        )

        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Button(
                onClick = onNext,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Orange),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) { Text("Next", color = Color.White) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LabeledDropdown(
    label: String,
    options: List<String>,
    initial: String,
    onPicked: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var current by remember { mutableStateOf(initial) }

    Text(label, color = LabelGrey, style = MaterialTheme.typography.bodyMedium)
    Spacer(Modifier.height(6.dp))

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = current,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                .fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DividerGrey,
                unfocusedBorderColor = DividerGrey,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { opt ->
                Text(
                    opt,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            current = opt
                            onPicked(opt)
                            expanded = false
                        }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    }
}
