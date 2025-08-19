package com.example.handyman.navigation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.handyman.ui.*
import com.example.handyman.R as HmR


/** Public route other modules can reference */
const val HandymanRootRoute = "handyman_root"

/** Internal routes for this feature */
private object HmRoutes {
    const val Welcome  = "hm_welcome"
    const val Form     = "hm_form"
    const val List     = "hm_list"
    const val Profile  = "hm_profile"
    const val Schedule = "hm_schedule"
    const val Checkout = "hm_checkout"
    const val Filters  = "hm_filters"
    const val Success  = "hm_success"
    const val Map      = "hm_map"
}

/** Entry composable for the Handyman feature */
@Composable
fun HandymanNavEntry(
    externalNav: NavHostController? = null,   // reserved (not used now)
    @DrawableRes heroResId: Int? = null,      // welcome/hero image
    @DrawableRes listBannerResId: Int? = null // optional list header image
) {
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = HmRoutes.Welcome
    ) {
        composable(HmRoutes.Welcome) {
            HandymanWelcomeScreen(
                onLetsGo = { nav.navigate(HmRoutes.Form) },
                onBack = { nav.popBackStack() },
                heroResId = heroResId ?: HmR.drawable.handyman_hero
            )
        }

        composable(HmRoutes.Form) {
            HandymanFormScreen(
                onNext = { nav.navigate(HmRoutes.List) },
                onBack = { nav.popBackStack() }
            )
        }

        // LIST
        composable(HmRoutes.List) {
            ProviderListScreen(
                onBack = { nav.popBackStack() },
                onOpenProvider = { nav.navigate(HmRoutes.Profile) },
                onOpenFilters  = { nav.navigate(HmRoutes.Filters) },
                onOpenMap      = { nav.navigate(HmRoutes.Map) }
            )
        }

        // MAP
        composable(HmRoutes.Map) {
            MapScreen(
                onBack = { nav.popBackStack() },
                onOpenList = {
                    // If List is underneath, just pop to it; otherwise navigate to it.
                    val popped = nav.popBackStack(HmRoutes.List, inclusive = false)
                    if (!popped) nav.navigate(HmRoutes.List)
                },
                onOpenProvider = { nav.navigate(HmRoutes.Profile) }   // ← tap mini card -> profile
            )
        }

        // PROFILE
        composable(HmRoutes.Profile) {
            ProviderProfileScreen(
                onBack = { nav.popBackStack() },
                onTakeAppointment = { nav.navigate(HmRoutes.Schedule) }
            )
        }

        // SCHEDULE
        composable(HmRoutes.Schedule) {
            ScheduleScreen(
                onBack = { nav.popBackStack() },
                onConfirm = { _, _ -> nav.navigate(HmRoutes.Checkout) }
            )
        }

        // CHECKOUT
        composable(HmRoutes.Checkout) {
            OrderCheckoutScreen(
                onBack = { nav.popBackStack() },
                onPlaceOrder = { nav.navigate(HmRoutes.Success) }
            )
        }

        // SUCCESS
        composable(HmRoutes.Success) {
            SuccessScreen(
                onGoHome = { nav.popBackStack(HmRoutes.Welcome, inclusive = false) }
            )
        }

        // FILTERS
        composable(HmRoutes.Filters) {
            FiltersScreen(
                onBack = { nav.popBackStack() },
                onApply = { newFilters ->
                    nav.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("filters", newFilters)
                    nav.popBackStack()
                }
            )
        }
    }
}
