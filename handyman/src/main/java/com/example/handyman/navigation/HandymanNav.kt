package com.example.handyman.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.handyman.ui.HandymanFormScreen
import com.example.handyman.ui.HandymanWelcomeScreen
import com.example.handyman.ui.OrderCheckoutScreen
import com.example.handyman.ui.ProviderListScreen
import com.example.handyman.ui.ProviderProfileScreen
import com.example.handyman.ui.FiltersScreen
import com.example.handyman.ui.FiltersState
import com.example.handyman.ui.ScheduleScreen
import com.example.handyman.ui.SuccessScreen

/** Public route name (if the app wants to refer to this feature) */
const val HandymanRootRoute = "handyman_root"

/** Internal routes for pages inside this feature */
private object HmRoutes {
    const val Welcome  = "hm_welcome"
    const val Form     = "hm_form"
    const val List     = "hm_list"
    const val Profile  = "hm_profile"
    const val Schedule = "hm_schedule"
    const val Checkout = "hm_checkout"
    const val Filters  = "hm_filters"
    const val Success  = "hm_success"
}

/**
 * Entry composable for the Handyman feature.
 * Central app can just show this composable and the rest of the flow stays inside.
 */
@Composable
fun HandymanNavEntry(
    externalNav: NavHostController? = null // (reserved for future use)
) {
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = HmRoutes.Welcome
    ) {
        composable(HmRoutes.Welcome) {
            HandymanWelcomeScreen(
                onLetsGo = { nav.navigate(HmRoutes.Form) }
            )
        }

        composable(HmRoutes.Form) {
            HandymanFormScreen(
                onNext = { nav.navigate(HmRoutes.List) },
                onBack = { nav.popBackStack() }
            )
        }

        composable(HmRoutes.List) {
            // Read filters passed back from FiltersScreen via SavedStateHandle
            val filters = nav.currentBackStackEntry
                ?.savedStateHandle
                ?.get<FiltersState>("filters")

            ProviderListScreen(
                onBack = { nav.popBackStack() },
                onOpenProvider = { /* item -> */ nav.navigate(HmRoutes.Profile) },
                onOpenFilters  = { nav.navigate(HmRoutes.Filters) },
                filters = filters // <-- pass to list screen
            )
        }

        composable(HmRoutes.Profile) {
            ProviderProfileScreen(
                onBack = { nav.popBackStack() },
                onTakeAppointment = { nav.navigate(HmRoutes.Schedule) } // go to time picker
            )
        }

        composable(HmRoutes.Schedule) {
            ScheduleScreen(
                onBack = { nav.popBackStack() },
                onConfirm = { _, _ ->
                    // You can stash date/time in a VM via SavedStateHandle later.
                    nav.navigate(HmRoutes.Checkout)
                }
            )
        }

        composable(HmRoutes.Checkout) {
            OrderCheckoutScreen(
                onBack = { nav.popBackStack() },
                onPlaceOrder = {
                    // Go to success page
                    nav.navigate(HmRoutes.Success)
                }
            )
        }

        composable(HmRoutes.Success) {
            SuccessScreen(
                onGoHome = {
                    // Pop back to the start of this feature flow
                    nav.popBackStack(HmRoutes.Welcome, inclusive = false)
                }
            )
        }

        composable(HmRoutes.Filters) {
            FiltersScreen(
                onBack = { nav.popBackStack() },
                onApply = { filters: FiltersState ->
                    // Pass filters back to the previous (List) destination
                    nav.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("filters", filters)
                    nav.popBackStack()
                }
            )
        }
    }
}
