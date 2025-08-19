package com.example.handyman.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.project.common_utils.components.BackArrowIcon
import com.project.common_utils.components.OrangeButton

/* --- design tokens (match the rest of your feature) --- */
private val Orange = Color(0xFFFF7A00)
private val DividerGrey = Color(0xFFE7E8EB)
private val LabelGrey = Color(0xFF9AA0A6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HandymanFormScreen(
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    // Field state
    var need by remember { mutableStateOf("Plumber") }
    var problem by remember { mutableStateOf("Do not work") }

    // Availability: 5 discrete stops → indices 0..4 map to 8/11/14/17/20
    val hours = listOf(8, 11, 14, 17, 20)
    var availabilityIndex by remember { mutableIntStateOf(2) } // default 14h like the mock

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        /* ---------- Top bar ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
                .height(40.dp)
        ) {
            Row(
                modifier = Modifier.matchParentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                BackArrowIcon(onClick = onBack, tint = Orange)
                Spacer(Modifier.width(12.dp))
                Text(
                    "Your handyman",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        /* ---------- Need dropdown ---------- */
        LabeledDropdown(
            label = "Need",
            options = listOf("Plumber", "Electrician", "Carpenter", "Painter", "Cleaner"),
            initial = need,
            onPicked = { need = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        /* ---------- Problem dropdown ---------- */
        LabeledDropdown(
            label = "Problem",
            options = listOf(
                "Do not work", "Leak / broken", "Installation", "Maintenance", "Other"
            ),
            initial = problem,
            onPicked = { problem = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(22.dp))

        /* ---------- Availability ---------- */
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

        // labels row (8h 11h 14h 17h 20h)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            hours.forEachIndexed { idx, h ->
                Text(
                    text = if (idx == availabilityIndex) "$h" + "h" else "$h" + "h",
                    style = if (idx == availabilityIndex)
                        MaterialTheme.typography.bodyMedium
                    else
                        MaterialTheme.typography.bodySmall,
                    color = if (idx == availabilityIndex) Color(0xFF30313A) else LabelGrey
                )
            }
        }

        // Discrete slider with 4 steps (5 positions)
        Slider(
            value = availabilityIndex.toFloat(),
            onValueChange = { availabilityIndex = it.toInt().coerceIn(0, hours.lastIndex) },
            valueRange = 0f..hours.lastIndex.toFloat(),
            steps = hours.size - 2, // 5 stops => 3 *internal* steps, but M3 expects size-2
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = Orange,
                activeTrackColor = Orange,
                inactiveTrackColor = DividerGrey
            )
        )

        Spacer(Modifier.weight(1f))

        /* ---------- CTA with drop shadow like the mock ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 22.dp)
                .shadow(20.dp, RoundedCornerShape(14.dp), clip = false),
            contentAlignment = Alignment.Center
        ) {
            // If your OrangeButton doesn't accept a modifier, wrapping keeps layout correct.
            Box(Modifier.fillMaxWidth()) {
                OrangeButton(
                    onClick = onNext,
                    text = "Next"
                )
            }
        }
    }
}

/* --------------------------------- helpers --------------------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LabeledDropdown(
    label: String,
    options: List<String>,
    initial: String,
    onPicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var current by remember { mutableStateOf(initial) }

    Text(label, color = LabelGrey, style = MaterialTheme.typography.bodyMedium)
    Spacer(Modifier.height(6.dp))

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = current,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
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

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { opt ->
                Text(
                    text = opt,
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
