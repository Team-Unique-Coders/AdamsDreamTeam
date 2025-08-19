package com.example.mechanic.ui

import androidx.compose.runtime.Composable
import com.example.handyman.ui.FiltersScreen
import com.example.handyman.ui.FiltersState

@Composable
fun MechanicFiltersScreen(
    onBack: () -> Unit,
    onApply: (FiltersState) -> Unit
) {
    FiltersScreen(onBack = onBack, onApply = onApply)
}
