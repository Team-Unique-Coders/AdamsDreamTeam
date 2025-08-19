package com.example.laundry.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.laundry.data.Provider
import com.example.laundry.data.providerKey
import com.example.laundry.navigation.LaundryDestinations
import com.project.common_utils.components.OrangeButton
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.Locale

private val Orange = Color(0xFFFF8800)
private val DimText = Color(0xFF9E9E9E)
private val Heading = Color(0xFF2E2E3E)

/* ───────────────────────── Public API ───────────────────────── */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleScreen(
    provider: Provider,
    providerName: String,
    providerPhotoUrl: String,
    initialDate: LocalDate = LocalDate.now(),
    startTime: LocalTime = LocalTime.of(13, 0),   // 01:00 PM
    endTime: LocalTime = LocalTime.of(19, 0),     // 07:00 PM
    slotMinutes: Int = 30,
    onOpen: (String) -> Unit = {},
    onBack: () -> Unit = {},
    onDateChanged: (LocalDate) -> Unit = {},
    onTimeSelected: (LocalDate, LocalTime) -> Unit = { _, _ -> }
) {
    var selectedDate by remember { mutableStateOf(initialDate) }
    var currentMonth by remember { mutableStateOf(YearMonth.from(initialDate)) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }

    val timeSlots = remember(startTime, endTime, slotMinutes) {
        generateSlots(startTime, endTime, slotMinutes)
    }
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            BottomAppBar {
                Spacer(Modifier.weight(1f))
                OrangeButton(
                    onClick = {

                        if (selectedTime != null) {
                            onTimeSelected(selectedDate, selectedTime!!)
                            val id = providerKey(provider)
                            onOpen(
                                LaundryDestinations.yourLaundry(
                                    id = id,
                                    date = selectedDate.toString(),  // e.g. 2025-08-18
                                    time = selectedTime.toString()              // e.g. 13:30
                                )
                            )
                        }else{
                            Toast.makeText(context,"Please Select Date and Time",
                                Toast.LENGTH_SHORT).show()
                        }
                    },
                   "Checkout"
                )
                Spacer(Modifier.weight(1f))
            }
        }
    ) { paddingValue ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            TopBar(providerName, providerPhotoUrl, onBack)

            MonthHeader(
                yearMonth = currentMonth,
                onPrev = {
                    currentMonth = currentMonth.minusMonths(1)
                },
                onNext = {
                    currentMonth = currentMonth.plusMonths(1)
                }
            )

            WeekRowPager(
                month = currentMonth,
                selected = selectedDate,
                onSelect = {
                    selectedDate = it
                    currentMonth = YearMonth.from(it)
                    onDateChanged(it)
                }
            )

            Divider()

            // Time slots list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(timeSlots) { time ->
                    TimeRow(
                        label = time.format(
                            DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())
                        ),
                        selected = selectedTime == time,
                        onClick = { selectedTime = time }
                    )
                    Divider()
                }
            }
        }
    }
}

/* ───────────────────────── UI pieces ───────────────────────── */

@Composable
private fun TopBar(
    providerName: String,
    photoUrl: String,
    onBack: () -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = 6.dp, start = 8.dp, end = 8.dp)
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Orange)
        }

        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = photoUrl,
                contentDescription = null,
                modifier = Modifier.size(26.dp).clip(CircleShape)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                providerName,
                style = MaterialTheme.typography.titleLarge,
                color = Heading
            )
        }
    }
    Spacer(Modifier.height(8.dp))
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MonthHeader(
    yearMonth: YearMonth,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrev) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Prev", tint = Orange)
        }
        Text(
            text = yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            color = Heading
        )
        IconButton(onClick = onNext) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Next", tint = Orange)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun WeekRowPager(
    month: YearMonth,
    selected: LocalDate?,
    onSelect: (LocalDate) -> Unit
) {
    val startOfMonth = month.atDay(1).with(DayOfWeek.SUNDAY)
    val endOfMonth = month.atEndOfMonth().with(DayOfWeek.SATURDAY)

    val allWeeks = generateSequence(startOfMonth) { it.plusWeeks(1) }
        .takeWhile { it <= endOfMonth }
        .toList()

    Column(Modifier.fillMaxWidth()) {
        // Days of week header
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            listOf("Sun","Mon","Tue","Wed","Thu","Fri","Sat").forEach { label ->
                Text(
                    text = label,
                    color = DimText,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .weight(1f),  // ✅ Equal 7 parts
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(Modifier.height(6.dp))

        // Calendar grid
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            allWeeks.forEach { startOfWeek ->
                Row(Modifier.fillMaxWidth()) {
                    (0..6).map { startOfWeek.plusDays(it.toLong()) }.forEach { date ->
                        val inThisMonth = YearMonth.from(date) == month
                        val isSelected = date == selected
                        val numberColor =
                            if (!inThisMonth) DimText.copy(alpha = 0.6f)
                            else if (isSelected) Color.White else Heading

                        Box(
                            modifier = Modifier
                                .weight(1f)     // ✅ Equal width for each day
                                .aspectRatio(1f) // Square cell
                                .clip(CircleShape)
                                .background(if (isSelected) Orange else Color.Transparent)
                                .clickable { onSelect(date) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = date.dayOfMonth.toString(),
                                color = numberColor,
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}



@Composable
private fun TimeRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() }
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (selected) "•" else " ",
            color = Orange,
            modifier = Modifier.width(20.dp),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = if (selected) Heading else Color(0xFF6B6B6B)
        )
    }
}

/* ───────────────────────── Helpers ───────────────────────── */

@RequiresApi(Build.VERSION_CODES.O)
private fun generateSlots(start: LocalTime, end: LocalTime, stepMinutes: Int): List<LocalTime> {
    require(stepMinutes > 0) { "stepMinutes must be > 0" }
    val slots = mutableListOf<LocalTime>()
    var t = start
    while (!t.isAfter(end)) {
        slots += t
        t = t.plusMinutes(stepMinutes.toLong())
    }
    return slots
}

/* ───────────────────────── Preview ───────────────────────── */

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true, heightDp = 900)
//@Composable
//private fun SchedulePreview() {
//    MaterialTheme {
//        ScheduleScreen(
//            providerName = "Jenny Jones",
//            providerPhotoUrl = "https://images.pexels.com/photos/532220/pexels-photo-532220.jpeg",
//            initialDate = LocalDate.of(2025, 8, 18)
//        )
//    }
//}
