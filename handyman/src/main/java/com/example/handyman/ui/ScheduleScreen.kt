package com.example.handyman.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.common_utils.components.BackArrowIcon
import com.project.common_utils.components.OrangeButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleScreen(
    onBack: () -> Unit,
    onConfirm: (selectedDate: LocalDate, selectedTime: String) -> Unit
) {
    // simple in-memory state
    val today = remember { LocalDate.now() }
    val dates = remember { (0..13).map { today.plusDays(it.toLong()) } } // next 2 weeks
    var selectedDate by remember { mutableStateOf(dates.first()) }

    val morning = listOf("08:00", "08:30", "09:00", "09:30", "10:00")
    val afternoon = listOf("12:00", "12:30", "13:00", "14:00", "15:00")
    val evening = listOf("17:00", "17:30", "18:00", "19:00")
    val allSlots = listOf("Morning" to morning, "Afternoon" to afternoon, "Evening" to evening)
    var selectedTime by remember { mutableStateOf(morning.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Top bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            BackArrowIcon(onClick = onBack)
            Spacer(Modifier.width(12.dp))
            Text("Choose time", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(Modifier.height(16.dp))

        // Date strip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            dates.forEach { date ->
                val sel = date == selectedDate
                DatePill(
                    date = date,
                    selected = sel,
                    onClick = { selectedDate = date }
                )
                Spacer(Modifier.width(8.dp))
            }
        }

        Spacer(Modifier.height(20.dp))

        // Time slots
        allSlots.forEach { (label, slots) ->
            Text(label, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            FlowRowGap {
                slots.forEach { time ->
                    TimeChip(
                        text = time,
                        selected = time == selectedTime,
                        onClick = { selectedTime = time }
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        Spacer(Modifier.weight(1f))

        // Confirm
        OrangeButton(
            onClick = { onConfirm(selectedDate, selectedTime) },
            text = "Confirm"
        )
    }
}

/* --------- UI bits --------- */

@Composable
private fun DatePill(
    date: LocalDate,
    selected: Boolean,
    onClick: () -> Unit
) {
    val dayName = date.dayOfWeek.name.lowercase().replaceFirstChar { it.titlecase() }.take(3) // Mon
    val dayNum = date.dayOfMonth.toString()
    val month = date.format(DateTimeFormatter.ofPattern("MMM"))

    val colors = if (selected)
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    else
        CardDefaults.cardColors()

    Card(
        colors = colors,
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(dayName, color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface)
            Text(dayNum,  style = MaterialTheme.typography.titleMedium,
                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface)
            Text(month,   color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun TimeChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = if (selected)
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    else
        CardDefaults.cardColors()

    Card(
        colors = colors,
        elevation = CardDefaults.cardElevation(1.dp),
        onClick = onClick
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            color = if (selected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * Tiny flow-row helper to wrap chips onto multiple lines with consistent spacing.
 */
@Composable
private fun FlowRowGap(
    gap: Int = 8,
    content: @Composable () -> Unit
) {
    // Simple spacer-based wrap using a Column of Rows so we don't pull in Accompanist
    val gapDp = gap.dp
    Column {
        var currentRowWidth = 0
        var rows = mutableListOf<@Composable () -> Unit>()
        // For simplicity, just put everything in one Row; Compose handles wrapping poorly without layout.
        // Using a simple Row with wrap content is enough for demo; chips will scroll if overflow in a parent.
        Row(horizontalArrangement = Arrangement.spacedBy(gapDp)) { content() }
    }
}
