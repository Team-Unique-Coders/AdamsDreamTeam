package com.example.handyman.navigation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import com.example.handyman.ui.*
import com.example.handyman.R as HmR

/** Public route */
const val HandymanRootRoute = "handyman_root"

/** Internal routes */
private object HmRoutes {
    const val WELCOME  = "hm_welcome"
    const val FORM     = "hm_form"
    const val LIST     = "hm_list"
    const val PROFILE  = "hm_profile"
    const val SCHEDULE = "hm_schedule"
    const val CHECKOUT = "hm_checkout"
    const val FILTERS  = "hm_filters"
    const val SUCCESS  = "hm_success"
    const val MAP      = "hm_map"
}

@Composable
fun HandymanNavEntry(
    externalNav: NavHostController? = null,   // reserved
    @DrawableRes heroResId: Int? = null,
    @DrawableRes listBannerResId: Int? = null
) {
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = HmRoutes.WELCOME
    ) {
        /* -------------------- WELCOME -------------------- */
        composable(HmRoutes.WELCOME) {
            HandymanWelcomeScreen(
                onLetsGo = { nav.navigate(HmRoutes.FORM) },
                onBack = { nav.popBackStack() },
                heroResId = heroResId ?: HmR.drawable.handyman_hero
            )
        }

        /* -------------------- FORM -------------------- */
        composable(HmRoutes.FORM) {
            HandymanFormScreen(
                // NOTE: your current screen takes () -> Unit
                onNext = { nav.navigate(HmRoutes.LIST) },
                onBack = { nav.popBackStack() }
            )
        }

        /* -------------------- LIST -------------------- */
        composable(HmRoutes.LIST) {
            val filters = nav.currentBackStackEntry
                ?.savedStateHandle
                ?.get<FiltersState>("filters")

            ProviderListScreen(
                onBack = { nav.popBackStack() },
                onOpenProvider = { p ->
                    nav.currentBackStackEntry?.savedStateHandle?.set("selectedProviderName", p.name)
                    nav.currentBackStackEntry?.savedStateHandle?.set("selectedProviderPrice", p.pricePerHour)
                    nav.navigate(HmRoutes.PROFILE)
                },
                onOpenFilters  = { nav.navigate(HmRoutes.FILTERS) },
                onOpenMap      = { nav.navigate(HmRoutes.MAP) },
                filters = filters,
                bannerResId = listBannerResId
            )
        }

        /* -------------------- MAP -------------------- */
        composable(HmRoutes.MAP) {
            MapScreen(
                onBack = { nav.popBackStack() },
                onOpenList = {
                    val popped = nav.popBackStack(HmRoutes.LIST, inclusive = false)
                    if (!popped) nav.navigate(HmRoutes.LIST)
                },
                onOpenProvider = { providerName ->
                    nav.currentBackStackEntry?.savedStateHandle?.set("selectedProviderName", providerName)
                    nav.navigate(HmRoutes.PROFILE)
                }
            )
        }

        /* -------------------- PROFILE -------------------- */
        composable(HmRoutes.PROFILE) {
            nav.previousBackStackEntry
                ?.savedStateHandle
                ?.get<String>("selectedProviderName")
                ?.let { sel ->
                    nav.currentBackStackEntry?.savedStateHandle?.set("selectedProviderName", sel)
                }

            val providerName = nav.currentBackStackEntry
                ?.savedStateHandle
                ?.get<String>("selectedProviderName") ?: "Jenny Jones"

            ProviderProfileScreen(
                onBack = { nav.popBackStack() },
                onTakeAppointment = { nav.navigate(HmRoutes.SCHEDULE) },
                providerName = providerName
            )
        }

        /* -------------------- SCHEDULE -------------------- */
        composable(HmRoutes.SCHEDULE) {
            val providerName = nav.previousBackStackEntry
                ?.savedStateHandle
                ?.get<String>("selectedProviderName") ?: "Jenny Jones"

            ScheduleScreen(
                onBack = { nav.popBackStack() },
                onConfirm = { date, time ->
                    nav.currentBackStackEntry?.savedStateHandle?.apply {
                        set("chosenDate", date)
                        set("chosenTime", time)
                    }
                    nav.navigate(HmRoutes.CHECKOUT)
                },
                providerName = providerName
            )
        }

        /* -------------------- CHECKOUT -------------------- */
        composable(HmRoutes.CHECKOUT) {
            val date = nav.previousBackStackEntry
                ?.savedStateHandle?.get<String>("chosenDate") ?: "20 March, Thu"
            val time = nav.previousBackStackEntry
                ?.savedStateHandle?.get<String>("chosenTime") ?: "14h"

            val providerName: String = runCatching {
                nav.getBackStackEntry(HmRoutes.PROFILE)
                    .savedStateHandle
                    .get<String>("selectedProviderName")
            }.getOrNull() ?: "Jenny Jones"

            val pricePerHour: Int = runCatching {
                nav.getBackStackEntry(HmRoutes.LIST)
                    .savedStateHandle
                    .get<Int>("selectedProviderPrice")
            }.getOrNull() ?: 15

            OrderCheckoutScreen(
                onBack = { nav.popBackStack() },
                onPlaceOrder = { nav.navigate(HmRoutes.SUCCESS) },
                providerName = providerName,
                pricePerHour = pricePerHour,
                dateLabel = "$date • $time"
            )
        }

        /* -------------------- SUCCESS -------------------- */
        composable(HmRoutes.SUCCESS) {
            SuccessScreen(
                onGoHome = {
                    val popped = nav.popBackStack(HmRoutes.LIST, inclusive = false)
                    if (!popped) nav.navigate(HmRoutes.LIST)
                }
            )
        }

        /* -------------------- FILTERS -------------------- */
        composable(HmRoutes.FILTERS) {
            FiltersScreen(
                onBack = { nav.popBackStack() },
                onApply = { newFilters ->
                    nav.previousBackStackEntry?.savedStateHandle?.set("filters", newFilters)
                    nav.popBackStack()
                }
            )
        }
    }
}
