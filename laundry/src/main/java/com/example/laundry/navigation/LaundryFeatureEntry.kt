package com.example.laundry.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController

/**
 * Redirect into the nested Laundry graph on the SAME central NavHostController.
 * No nested NavHost here (prevents ViewModelStore crash).
 */
@Composable
fun LaundryFeatureEntry(
    nav: NavHostController,
    popUpSelf: Boolean = true,
    selfRoute: String? = null
) {
    LaunchedEffect(Unit) {
        nav.navigate(LaundryDestinations.GRAPH) {
            if (popUpSelf && selfRoute != null) {
                popUpTo(selfRoute) { inclusive = true }
            }
            launchSingleTop = true
        }
    }
}
