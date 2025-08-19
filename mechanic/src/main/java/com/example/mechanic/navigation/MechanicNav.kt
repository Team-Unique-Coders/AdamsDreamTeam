package com.example.mechanic.navigation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mechanic.ui.*
import com.example.handyman.ui.*     // reuse shared models & map
import com.example.mechanic.R as MeR

/** Public route */
const val MechanicRootRoute = "mechanic_root"

/** Internal routes */
private object McRoutes {
    const val WELCOME  = "mc_welcome"
    const val FORM     = "mc_form"
    const val LIST     = "mc_list"
    const val PROFILE  = "mc_profile"
    const val SCHEDULE = "mc_schedule"
    const val CHECKOUT = "mc_checkout"
    const val FILTERS  = "mc_filters"
    const val SUCCESS  = "mc_success"
    const val MAP      = "mc_map"
}

@Composable
fun MechanicNavEntry(
    externalNav: NavHostController? = null,
    @DrawableRes heroResId: Int? = null,
    @DrawableRes listBannerResId: Int? = null
) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = McRoutes.WELCOME) {

        composable(McRoutes.WELCOME) {
            MechanicWelcomeScreen(
                onLetsGo = { nav.navigate(McRoutes.FORM) },
                onBack   = { nav.popBackStack() },
                heroResId = heroResId ?: MeR.drawable.mechanic_hero
            )
        }

        composable(McRoutes.FORM) {
            MechanicFormScreen(
                onNext = { nav.navigate(McRoutes.LIST) },
                onBack = { nav.popBackStack() }
            )
        }

        composable(McRoutes.LIST) {
            val filters = nav.currentBackStackEntry
                ?.savedStateHandle
                ?.get<FiltersState>("filters")

            MechanicProviderListScreen(
                bannerResId = listBannerResId ?: MeR.drawable.mechanic_list,
                filters = filters,
                onBack = { nav.popBackStack() },
                onOpenFilters = { nav.navigate(McRoutes.FILTERS) },
                onOpenMap = { nav.navigate(McRoutes.MAP) },
                onOpenProvider = { p ->
                    nav.currentBackStackEntry?.savedStateHandle?.set("selectedProviderName", p.name)
                    nav.currentBackStackEntry?.savedStateHandle?.set("selectedProviderPrice", p.pricePerHour)
                    nav.navigate(McRoutes.PROFILE)
                }
            )
        }

        composable(McRoutes.MAP) {
            // reuse the same map screen; its visuals match your mock
            MapScreen(
                onBack = { nav.popBackStack() },
                onOpenList = {
                    val popped = nav.popBackStack(McRoutes.LIST, inclusive = false)
                    if (!popped) nav.navigate(McRoutes.LIST)
                },
                onOpenProvider = { providerName ->
                    nav.currentBackStackEntry?.savedStateHandle?.set("selectedProviderName", providerName)
                    nav.navigate(McRoutes.PROFILE)
                }
            )
        }

        composable(McRoutes.PROFILE) {
            nav.previousBackStackEntry?.savedStateHandle
                ?.get<String>("selectedProviderName")
                ?.let { sel ->
                    nav.currentBackStackEntry?.savedStateHandle?.set("selectedProviderName", sel)
                }

            val providerName = nav.currentBackStackEntry
                ?.savedStateHandle
                ?.get<String>("selectedProviderName") ?: "Jenny Jones"

            MechanicProviderProfileScreen(
                providerName = providerName,
                onBack = { nav.popBackStack() },
                onTakeAppointment = { nav.navigate(McRoutes.SCHEDULE) }
            )
        }

        composable(McRoutes.SCHEDULE) {
            val providerName = nav.previousBackStackEntry
                ?.savedStateHandle
                ?.get<String>("selectedProviderName") ?: "Jenny Jones"

            MechanicScheduleScreen(
                providerName = providerName,
                onBack = { nav.popBackStack() },
                onConfirm = { date, time ->
                    nav.currentBackStackEntry?.savedStateHandle?.apply {
                        set("chosenDate", date)
                        set("chosenTime", time)
                    }
                    nav.navigate(McRoutes.CHECKOUT)
                }
            )
        }

        composable(McRoutes.CHECKOUT) {
            val date = nav.previousBackStackEntry?.savedStateHandle?.get<String>("chosenDate") ?: "20 March, Thu"
            val time = nav.previousBackStackEntry?.savedStateHandle?.get<String>("chosenTime") ?: "14h"

            val providerName: String = runCatching {
                nav.getBackStackEntry(McRoutes.PROFILE)
                    .savedStateHandle.get<String>("selectedProviderName")
            }.getOrNull() ?: "Jenny Jones"

            val pricePerHour: Int = runCatching {
                nav.getBackStackEntry(McRoutes.LIST)
                    .savedStateHandle.get<Int>("selectedProviderPrice")
            }.getOrNull() ?: 15

            MechanicOrderCheckoutScreen(
                onBack = { nav.popBackStack() },
                onPlaceOrder = { nav.navigate(McRoutes.SUCCESS) },
                providerName = providerName,
                pricePerHour = pricePerHour,
                dateLabel = "$date • $time"
            )
        }

        composable(McRoutes.SUCCESS) {
            MechanicSuccessScreen(
                onGoHome = {
                    val popped = nav.popBackStack(McRoutes.LIST, inclusive = false)
                    if (!popped) nav.navigate(McRoutes.LIST)
                }
            )
        }

        composable(McRoutes.FILTERS) {
            MechanicFiltersScreen(
                onBack = { nav.popBackStack() },
                onApply = { newFilters ->
                    nav.previousBackStackEntry?.savedStateHandle?.set("filters", newFilters)
                    nav.popBackStack()
                }
            )
        }
    }
}
