package com.example.mechanic.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.common_utils.BackArrowIcon
import com.project.common_utils.OrangeButton

@Composable
fun MechanicScheduleScreen(
    onBack: () -> Unit,
    onConfirm: (day: String, time: String) -> Unit
) {
    var day by remember { mutableStateOf("Thu 20 Mar") }
    var time by remember { mutableStateOf("14:00") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BackArrowIcon(onClick = onBack)
            Spacer(Modifier.width(12.dp))
            Text("Choose time", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(Modifier.height(16.dp))

        // ultra-simple pickers (stub)
        OutlinedTextField(value = day, onValueChange = { day = it }, label = { Text("Day") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(value = time, onValueChange = { time = it }, label = { Text("Time") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.weight(1f))
        OrangeButton(onClick = { onConfirm(day, time) }, text = "Confirm")
    }
}
