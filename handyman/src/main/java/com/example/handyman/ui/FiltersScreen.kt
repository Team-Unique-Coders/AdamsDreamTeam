package com.example.handyman.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.project.common_utils.components.BackArrowIcon
import com.project.common_utils.components.ReviewStars

//  Bright vivid orange (mockup style)
private val Orange = Color(0xFFFF7A00)
private val DividerGrey = Color(0xFFE7E8EB)
private val LabelGrey = Color(0xFF9AA0A6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersScreen(
    onBack: () -> Unit,
    onApply: (FiltersState) -> Unit
) {
    var sort by remember { mutableStateOf("Recommend") }
    var expanded by remember { mutableStateOf(false) }
    var price by remember { mutableFloatStateOf(30f) }   // 0..60
    var rating by remember { mutableFloatStateOf(4.5f) } // show half like mock

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        /* ---------- Top bar (back | centered title | Clear) ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
                .height(40.dp)
        ) {
            Row(
                modifier = Modifier.matchParentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BackArrowIcon(onClick = onBack, tint = Orange)
                Spacer(Modifier.width(1.dp)) // keeps Row balanced
                Text(
                    "Clear",
                    color = Orange,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier.clickable {
                        sort = "Recommend"
                        price = 30f
                        rating = 4.5f
                    }
                )
            }
            Text(
                "Filters",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFF30313A),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        /* ---------- Sort by (card-like field) ---------- */
        Text("Sort by", color = LabelGrey, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(6.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            @Suppress("DEPRECATION")
            OutlinedTextField(
                value = sort,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DividerGrey,
                    unfocusedBorderColor = DividerGrey,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                )
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = false)
            ) {
                listOf("Recommend", "Rating", "Price low → high", "Price high → low").forEach { opt ->
                    DropdownMenuItem(
                        text = { Text(opt) },
                        onClick = {
                            sort = opt
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(18.dp))
        SectionHeader("Price/hour")

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("0", color = Color.Black)
            Text(price.toInt().toString(), color = Color.Black)
            Text("60", color = LabelGrey)
        }
        Spacer(Modifier.height(8.dp))

        Slider(
            value = price,
            onValueChange = { price = it },
            valueRange = 0f..60f,
            steps = 0,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = Orange,
                activeTrackColor = Orange,
                inactiveTrackColor = DividerGrey
            )
        )

        Spacer(Modifier.height(14.dp))
        SectionHeader("Rate")

        Spacer(Modifier.height(8.dp))
        ReviewStars(
            rating = rating,
            onRatingChange = { rating = it },
            size = 28.dp,
            activeColor = Orange,
            inactiveColor = Color(0xFFDADDE1)
        )

        Spacer(Modifier.weight(1f))

        /* ---------- Apply button ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 22.dp)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(14.dp),
                    clip = false
                ),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    onApply(
                        FiltersState(
                            pricePerHourMax = price.toInt(),
                            minRating = rating,
                            maxDistanceMeters = 2000,
                            categories = emptyList()
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text(
                    "Apply",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}

/* ---------- tiny helpers ---------- */

@Composable
private fun SectionHeader(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = LabelGrey, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.width(10.dp))
        Box(
            Modifier
                .weight(1f)
                .height(1.dp)
                .background(DividerGrey)
        )
    }
}
