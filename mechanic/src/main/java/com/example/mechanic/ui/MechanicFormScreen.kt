package com.example.mechanic.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.common_utils.ThreeValueSlider
import com.project.common_utils.components.BackArrowIcon
import com.project.common_utils.components.OrangeButton
import com.project.common_utils.components.SearchField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MechanicFormScreen(onNext: () -> Unit, onBack: () -> Unit) {
    var query by remember { mutableStateOf("") }
    var type  by remember { mutableStateOf("Car") }
    var model by remember { mutableStateOf("Lexus") }
    var year  by remember { mutableStateOf("2016") }
    var motor by remember { mutableStateOf("Gasoline") }

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            BackArrowIcon(onClick = onBack)
            Spacer(Modifier.width(12.dp))
            Text("Your mechanic", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(Modifier.height(16.dp))
        SearchField(query, { query = it }, "Search service or garage...", Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))

        DropDownField("Type",  listOf("Car","Motorcycle","Truck"), type)  { type = it }
        Spacer(Modifier.height(12.dp))
        DropDownField("Model", listOf("Lexus","Toyota","BMW","Honda"),   model) { model = it }
        Spacer(Modifier.height(12.dp))
        DropDownField("Year",  (2005..2025).map { it.toString() },       year)  { year = it }
        Spacer(Modifier.height(12.dp))
        DropDownField("Motor", listOf("Gasoline","Diesel","Electric","Hybrid"), motor) { motor = it }

        Spacer(Modifier.height(24.dp))
        Text("Availability", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        ThreeValueSlider(
            valueRange = 0f..100f, startValue = 0f, midValue = 50f, endValue = 100f,
            onValueChange = { /* ignore for now */ },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(32.dp))
        OrangeButton(onClick = onNext, text = "Next")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropDownField(
    label: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selected, onValueChange = {}, readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { opt ->
                DropdownMenuItem(onClick = { onSelected(opt); expanded = false }, text = { Text(opt) })
            }
        }
    }
}
