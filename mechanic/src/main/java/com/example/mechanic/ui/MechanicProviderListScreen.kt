package com.example.mechanic.ui

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.example.handyman.ui.FiltersState
import com.example.handyman.ui.ProviderListScreen
import com.example.handyman.ui.ProviderUi
import com.example.mechanic.R as MeR

@Composable
fun MechanicProviderListScreen(
    onBack: () -> Unit,
    onOpenProvider: (ProviderUi) -> Unit,
    onOpenFilters: () -> Unit,
    onOpenMap: () -> Unit,
    filters: FiltersState? = null,
    @DrawableRes bannerResId: Int = MeR.drawable.mechanic_list
) {
    ProviderListScreen(
        onBack = onBack,
        onOpenProvider = onOpenProvider,
        onOpenFilters = onOpenFilters,
        onOpenMap = onOpenMap,
        filters = filters,
        bannerResId = bannerResId
    )
}
