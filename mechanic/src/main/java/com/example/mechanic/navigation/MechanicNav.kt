package com.example.mechanic.navigation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mechanic.ui.*     // the mechanic screens below

// If you share some generic screens (MapScreen, ScheduleScreen, etc.) in a common/ui pkg,
// you can import them from there instead.

private object MechRoutes {
    const val Welcome  = "mech_welcome"
    const val Form     = "mech_form"
    const val List     = "mech_list"
    const val Profile  = "mech_profile"
    const val Schedule = "mech_schedule"
    const val Checkout = "mech_checkout"
    const val Filters  = "mech_filters"
    const val Success  = "mech_success"
    const val Map      = "mech_map"
}

@Composable
fun MechanicNavEntry(
    externalNav: NavHostController? = null,
    @DrawableRes heroResId: Int? = null,
    @DrawableRes listBannerResId: Int? = null
) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = MechRoutes.Welcome) {
        /* --------- WELCOME --------- */
        composable(MechRoutes.Welcome) {
            MechanicWelcomeScreen(
                onLetsGo = { nav.navigate(MechRoutes.Form) },
                onBack = { nav.popBackStack() },
                heroResId = heroResId
            )
        }

        /* --------- FORM --------- */
        composable(MechRoutes.Form) {
            MechanicFormScreen(
                onNext = { nav.navigate(MechRoutes.List) },
                onBack = { nav.popBackStack() }
            )
        }

        /* --------- LIST --------- */
        composable(MechRoutes.List) {
            val filters = nav.previousBackStackEntry
                ?.savedStateHandle
                ?.get<FiltersState>("filters")

            MechanicListScreen(
                onBack = { nav.popBackStack() },
                onOpenProvider = { nav.navigate(MechRoutes.Profile) },
                onOpenFilters  = { nav.navigate(MechRoutes.Filters) },
                onOpenMap      = { nav.navigate(MechRoutes.Map) },
                filters = filters,
                bannerResId = listBannerResId
            )
        }

        /* --------- MAP --------- */
        composable(MechRoutes.Map) {
            MechanicMapScreen(
                onBack = { nav.popBackStack() },
                onOpenList = {
                    val popped = nav.popBackStack(MechRoutes.List, inclusive = false)
                    if (!popped) nav.navigate(MechRoutes.List)
                },
                onOpenProvider = { nav.navigate(MechRoutes.Profile) }
            )
        }

        /* --------- PROFILE --------- */
        composable(MechRoutes.Profile) {
            MechanicProfileScreen(
                onBack = { nav.popBackStack() },
                onTakeAppointment = { nav.navigate(MechRoutes.Schedule) }
            )
        }

        /* --------- SCHEDULE --------- */
        composable(MechRoutes.Schedule) {
            MechanicScheduleScreen(
                onBack = { nav.popBackStack() },
                onConfirm = { _, _ -> nav.navigate(MechRoutes.Checkout) }
            )
        }

        /* --------- CHECKOUT --------- */
        composable(MechRoutes.Checkout) {
            MechanicCheckoutScreen(
                onBack = { nav.popBackStack() },
                onPlaceOrder = { nav.navigate(MechRoutes.Success) }
            )
        }

        /* --------- SUCCESS --------- */
        composable(MechRoutes.Success) {
            MechanicSuccessScreen(
                // same behavior we set for Handyman: go back to the List
                onGoHome = {
                    val popped = nav.popBackStack(MechRoutes.List, inclusive = false)
                    if (!popped) nav.navigate(MechRoutes.List)
                }
            )
        }

        /* --------- FILTERS --------- */
        composable(MechRoutes.Filters) {
            MechanicFiltersScreen(
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
