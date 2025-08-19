package com.example.doctor.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.project.common_utils.R
import com.project.common_utils.components.BackArrowIcon
import com.project.common_utils.components.ButtonIcon

@Composable
fun DoctorGenderScreen(onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ButtonIcon(
            R.drawable.home,
            onClick = onBack,
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.weight(1f))
        ButtonIcon(
            R.drawable.options,
            onClick = { },
            tint = Color.Gray
        )
        ButtonIcon(
            R.drawable.pin,
            onClick = { },
            tint = Color.Gray
        )
    }
}