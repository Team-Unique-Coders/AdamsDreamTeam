package com.example.learn.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.learn.ui.FiltersState
import com.example.learn.ui.LearnFiltersScreen
import com.example.learn.ui.LearnFormScreen
import com.example.learn.ui.LearnOrderCheckoutScreen
import com.example.learn.ui.LearnProviderListScreen
import com.example.learn.ui.LearnProviderProfileScreen
import com.example.learn.ui.LearnScheduleScreen
import com.example.learn.ui.LearnSuccessScreen
import com.example.learn.ui.LearnWelcomeScreen
import java.time.LocalDate

const val learnRootRoute = "learn_root"

private object LearnRoutes {
    const val Welcome = "learn_welcome"
    const val Form = "learn_form"
    const val List = "learn_list"
    const val Profile = "learn_profile"
    const val Filters = "learn_filters"
    const val Schedule = "learn_schedule"
    const val Checkout = "learn_checkout"
    const val Success = "learn_success"
}

@Composable
fun LearnNavEntry(externalNav: NavHostController? = null, onClose: () -> Unit) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = LearnRoutes.Welcome) {
        composable(LearnRoutes.Welcome) {
            LearnWelcomeScreen(
                onLetsGo = { nav.navigate(LearnRoutes.Form) },
                onBack =  onClose
            )
        }
        composable(LearnRoutes.Form) {
            LearnFormScreen(
                onNext = { nav.navigate(LearnRoutes.List) },
                onBack = onClose
            )
        }
        composable(LearnRoutes.List) {
            val filters = nav.currentBackStackEntry
                ?.savedStateHandle
                ?.get<FiltersState>("filters")

            LearnProviderListScreen(
                onBack = { nav.popBackStack() },
                onOpenProvider = { /* item -> */ nav.navigate(LearnRoutes.Profile) },
                onOpenFilters = { nav.navigate(LearnRoutes.Filters) },
                filters = filters
            )
        }
        composable(LearnRoutes.Profile) {
            LearnProviderProfileScreen(
                onBack = { nav.popBackStack() },
                onTakeAppointment = { nav.navigate(LearnRoutes.Schedule) }
            )
        }
        composable(LearnRoutes.Filters) {
            LearnFiltersScreen(
                onBack = { nav.popBackStack() },
                onApply = { filters ->
                    nav.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("filters", filters)
                    nav.popBackStack()
                }
            )
        }
        composable(LearnRoutes.Schedule) {
            LearnScheduleScreen(
                onBack = { nav.popBackStack() },
                onConfirm = { _, _ ->
                    nav.navigate(LearnRoutes.Success)
                }
            )
        }
        composable(LearnRoutes.Checkout) {
            LearnOrderCheckoutScreen(
                onBack = { nav.popBackStack() },
                onPlaceOrder = {
                    nav.navigate(LearnRoutes.Success)
                }
            )
        }
        composable(LearnRoutes.Success) {
            LearnSuccessScreen(
                onGoHome = {
                    nav.popBackStack(LearnRoutes.Welcome, inclusive = false)
                }
            )
        }
    }
}