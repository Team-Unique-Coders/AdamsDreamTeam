package com.example.mechanic.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mechanic.ui.*

private object McRoutes {
    const val Welcome  = "mc_welcome"
    const val Form     = "mc_form"
    const val List     = "mc_list"
    const val Profile  = "mc_profile"
    const val Schedule = "mc_schedule"
    const val Checkout = "mc_checkout"
    const val Success  = "mc_success"
    const val Filters  = "mc_filters"
}

@Composable
fun MechanicNavEntry() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = McRoutes.Welcome) {
        composable(McRoutes.Welcome) {
            MechanicWelcomeScreen(onLetsGo = { nav.navigate(McRoutes.Form) })
        }
        composable(McRoutes.Form) {
            MechanicFormScreen(
                onNext = { nav.navigate(McRoutes.List) },
                onBack = { nav.popBackStack() }
            )
        }
        composable(McRoutes.List) {
            MechanicProviderListScreen(
                onBack = { nav.popBackStack() },
                onOpenFilters = { nav.navigate(McRoutes.Filters) },
                onOpenProvider = { /* id -> */ nav.navigate(McRoutes.Profile) }
            )
        }
        composable(McRoutes.Profile) {
            MechanicProviderProfileScreen(
                onBack = { nav.popBackStack() },
                onTakeAppointment = { nav.navigate(McRoutes.Schedule) }
            )
        }
        composable(McRoutes.Schedule) {
            MechanicScheduleScreen(
                onBack = { nav.popBackStack() },
                onConfirm = { _, _ -> nav.navigate(McRoutes.Checkout) }
            )
        }
        composable(McRoutes.Checkout) {
            MechanicOrderCheckoutScreen(
                onBack = { nav.popBackStack() },
                onPlaceOrder = { nav.navigate(McRoutes.Success) }
            )
        }
        composable(McRoutes.Success) {
            MechanicSuccessScreen(
                onGoHome = {
                    nav.popBackStack(McRoutes.Welcome, inclusive = false)
                }
            )
        }
        composable(McRoutes.Filters) {
            MechanicFiltersScreen(
                onBack = { nav.popBackStack() },
                onApply = { /* pass via savedStateHandle later */ nav.popBackStack() }
            )
        }
    }
}
