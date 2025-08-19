package com.example.handyman.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
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
import com.project.common_utils.components.BackArrowIcon
import com.example.handyman.R as HmR

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
    var selectedDay by remember { mutableStateOf(6) }

    val times = listOf(
        "01:00 PM", "01:30 PM", "02:00 PM", "02:30 PM",
        "03:00 PM", "03:30 PM", "04:00 PM"
    )

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(4.dp))
                Text(
                    "March 2019",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = subtle
                )
            }

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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                (4..10).forEach { day ->
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
            }

            Divider()

            Column(Modifier.fillMaxWidth()) {
                times.forEachIndexed { i, time ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clickable { onConfirm("Tue, Mar $selectedDay", time) }
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (i == 0) {
                            Box(
                                Modifier.size(6.dp).clip(CircleShape)
                                    .background(Color(0xFF25314C))
                            )
                        } else {
                            Spacer(Modifier.width(6.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(time, fontSize = 16.sp)
                    }
                    Divider()
                }
            }
        }
    }
}
