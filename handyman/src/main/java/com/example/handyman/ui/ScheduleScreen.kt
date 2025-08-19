package com.example.handyman.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.handyman.R as HmR
import com.project.common_utils.components.BackArrowIcon
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    onBack: () -> Unit,
    onConfirm: (dateLabel: String, timeLabel: String) -> Unit,
    providerName: String = "Jenny Jones",
    @DrawableRes providerAvatar: Int = HmR.drawable.profilepicture
) {
    val orange = Color(0xFFFF7A00)
    val subtle = Color(0xFF8F9399)

    /* ------------ Month constants: August 2025 (no java.time required) ------------ */
    val monthHeader = "August 2025"
    val monthShort = "Aug"
    val year = 2025
    val monthZeroBased = Calendar.AUGUST // 7

    // Helpers using Calendar (available on all Android)
    fun monthLength(): Int {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthZeroBased)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH) // 31
    }

    fun firstSundayDay(): Int {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthZeroBased)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        val dow = cal.get(Calendar.DAY_OF_WEEK) // 1=Sun..7=Sat
        val offset = (Calendar.SUNDAY - dow + 7) % 7
        return 1 + offset // first day-of-month that is a Sunday
    }

    fun dayOfWeekName(dayOfMonth: Int): String {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthZeroBased)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        // Calendar: 1=Sun..7=Sat
        return when (cal.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "Sun"
            Calendar.MONDAY -> "Mon"
            Calendar.TUESDAY -> "Tue"
            Calendar.WEDNESDAY -> "Wed"
            Calendar.THURSDAY -> "Thu"
            Calendar.FRIDAY -> "Fri"
            else -> "Sat"
        }
    }

    val maxDay = remember { monthLength() }              // 31
    var weekStart by remember { mutableStateOf(firstSundayDay()) } // start on the first Sunday
    fun clampWeekStart(v: Int): Int = v.coerceIn(1, (maxDay - 6).coerceAtLeast(1))

    // Visible days for the current week (Sun..Sat)
    fun visibleDays(start: Int): List<Int> =
        (start..(start + 6)).mapNotNull { d -> if (d in 1..maxDay) d else null }

    var days by remember { mutableStateOf(visibleDays(weekStart)) }

    // Selected day (defaults to the 3rd cell in the first visible week to mimic your mock)
    var selectedDay by remember { mutableStateOf((days.getOrNull(2) ?: weekStart)) }
    LaunchedEffect(weekStart) {
        days = visibleDays(weekStart)
        if (selectedDay !in days) selectedDay = days.first()
    }

    val allTimes = remember {
        listOf(
            "01:00 PM", "01:30 PM", "02:00 PM", "02:30 PM",
            "03:00 PM", "03:30 PM", "04:00 PM"
        )
    }

    // Simple rotating availability — different between Jenny & Jean
    fun availableTimesFor(provider: String, dayNumber: Int): Set<String> {
        val rotateBy = dayNumber % 3
        fun rotate(list: List<String>, k: Int) =
            if (k == 0) list else list.drop(k) + list.take(k)

        return if (provider.contains("Jean", ignoreCase = true)) {
            rotate(listOf("01:30 PM", "02:30 PM", "03:30 PM"), rotateBy).toSet()
        } else {
            val blocked = rotate(listOf("02:30 PM", "03:30 PM"), rotateBy).take(1).toSet()
            allTimes.filterNot { it in blocked }.toSet()
        }
    }

    val selectedDayHeader = "${dayOfWeekName(selectedDay)}, $monthShort $selectedDay, $year"

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = { BackArrowIcon(onClick = onBack) },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(providerAvatar),
                            contentDescription = null,
                            modifier = Modifier.size(22.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(providerName, fontWeight = FontWeight.SemiBold)
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 16.dp)
        ) {
            /* ----- Month header with working chevrons (page weeks) ----- */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.ChevronLeft,
                    contentDescription = "Previous week",
                    tint = subtle,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            weekStart = clampWeekStart(weekStart - 7)
                        }
                )
                Text(
                    monthHeader,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = "Next week",
                    tint = subtle,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            weekStart = clampWeekStart(weekStart + 7)
                        }
                )
            }

            // Weekday labels (static Sun..Sat)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Sun","Mon","Tue","Wed","Thu","Fri","Sat").forEach {
                    Text(it, color = subtle, fontSize = 13.sp)
                }
            }

            // Numbers row (current visible week) with orange pill selection
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                days.forEach { day ->
                    val selected = day == selectedDay
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(if (selected) orange else Color.Transparent)
                            .clickable { selectedDay = day },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            day.toString(),
                            color = if (selected) Color.White else Color.Unspecified,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
                // if month end leaves <7 days, fill with spacers to keep layout aligned
                repeat(7 - days.size) {
                    Spacer(Modifier.size(34.dp))
                }
            }

            Divider()

            // Availability for the chosen day
            val available = remember(providerName, selectedDay) {
                availableTimesFor(providerName, selectedDay)
            }

            Column(Modifier.fillMaxWidth()) {
                allTimes.forEachIndexed { i, time ->
                    val isAvailable = time in available
                    val alpha = if (isAvailable) 1f else 0.38f

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .let { base ->
                                if (isAvailable) base.clickable {
                                    onConfirm(selectedDayHeader, time) // e.g., "Wed, Aug 6, 2025"
                                } else base
                            }
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val dotColor =
                            if (isAvailable) Color(0xFF25314C) else Color(0xFF25314C).copy(alpha = 0.38f)
                        if (i == 0) {
                            Box(
                                Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(dotColor)
                            )
                        } else {
                            Spacer(Modifier.width(6.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(time, fontSize = 16.sp, color = Color.Black.copy(alpha = alpha))
                    }
                    Divider()
                }
            }
        }
    }
}
