package com.example.handyman.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.common_utils.BackArrowIcon
import com.project.common_utils.OrangeButton
import com.project.common_utils.SearchField
import com.project.common_utils.ThreeValueSlider

@Composable
fun HandymanFormScreen(
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            BackArrowIcon(onClick = onBack)
            Spacer(Modifier.width(12.dp))
            Text(
                text = "Your handyman",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(Modifier.height(16.dp))

        // Search
        var query by remember { mutableStateOf("") }
        SearchField(
            value = query,
            onValueChange = { query = it },
            placeholder = "Search service or pro...",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // Dropdowns
        var service by remember { mutableStateOf("Plumber") }
        var location by remember { mutableStateOf("New York") }

        DropdownField(
            label = "Service type",
            options = listOf("Plumber", "Electrician", "Carpenter", "Painter", "Cleaner"),
            selected = service,
            onSelected = { service = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        DropdownField(
            label = "Location",
            options = listOf("New York", "San Francisco", "Austin", "Seattle", "Chicago"),
            selected = location,
            onSelected = { location = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        // Availability slider
        Text(text = "Availability", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        ThreeValueSlider(
            valueRange = 0f..100f,
            startValue = 0f,
            midValue = 50f,
            endValue = 100f,
            onValueChange = { /* capture if needed */ },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(32.dp))

        // CTA
        OrangeButton(onClick = onNext, text = "Find handyman")
    }
}

/* -------- Dropdown helper (Material3 DropdownMenu + clickable Text) ---------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownField(
    label: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()      // anchor the menu to this field
                .fillMaxWidth()
        )

        // Use plain DropdownMenu + clickable Text rows
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelected(option)
                            expanded = false
                        }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    }
}
