package com.example.laundry.navigation

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.laundry.data.LaundryOptions
import com.example.laundry.data.LaundryViewModel
import com.example.laundry.data.providerKey
import com.example.laundry.screens.FilterScreen
import com.example.laundry.screens.LaundryHomeScreen
import com.example.laundry.screens.LaundryMapScreen
import com.example.laundry.screens.LaundryScreen
import com.example.laundry.screens.Order
import com.example.laundry.screens.OrderItem
import com.example.laundry.screens.OrderScreen
import com.example.laundry.screens.ProviderDetailScreen
import com.example.laundry.screens.ScheduleScreen
import com.example.laundry.screens.ViewOrderScreen
import com.example.laundry.screens.YourLaundryScreen
import java.time.LocalDate

/* Nested graph routes */
object LaundryDestinations {
    const val GRAPH = "laundry"        // nested graph route
    const val LIST  = "laundry/list"   // start destination -> LaundryScreen
    const val HOME = "laundry/home"
    const val FILTER = "laundry/filter"

    const val DETAILS = "laundry/details/{id}"
    fun details(id: String) = "laundry/details/$id"

    const val MAPSCREEN = "laundry/maps"

    const val SCHEDULE = "laundry/schedule/{id}"
    fun schedule(id: String) = "laundry/schedule/$id"

    const val YOURLAUNDRY = "laundry/your/{id}/{date}/{time}"
    fun yourLaundry(id: String, date: String, time: String): String =
        "laundry/your/$id/${Uri.encode(date)}/${Uri.encode(time)}"

    const val ORDER = "laundry/order/{id}/{date}/{time}"
    fun order(id: String, date: String, time: String): String =
        "laundry/order/$id/${Uri.encode(date)}/${Uri.encode(time)}"

    // Key used to pass LaundryOptions via SavedStateHandle (between YourLaundry -> Order)
    const val OPTS_KEY = "laundry_opts"
    const val VIEW_ORDERS = "laundry/orders"
}

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun graphViewModel(nav: NavHostController): LaundryViewModel {
    // Scope the VM to the "laundry" graph so all its destinations share it
    val graphEntry = remember(nav) { nav.getBackStackEntry(LaundryDestinations.GRAPH) }
    return viewModel(graphEntry)
}

/**
 * Register the Laundry nested graph.
 * Accept an onOpen callback so Laundry screens can navigate:
 * onOpen = { route -> nav.navigate(route) }
 */
@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.addLaundryGraph(
    nav: NavHostController,
    onOpen: (String) -> Unit = { route -> nav.navigate(route) }, // default: use the same controller
) {
    navigation(
        startDestination = LaundryDestinations.LIST,
        route = LaundryDestinations.GRAPH
    ) {
        composable(LaundryDestinations.LIST) {
            LaundryScreen(onOpen = onOpen)
        }

        composable(LaundryDestinations.HOME) {
            val vm = graphViewModel(nav)
            val providers = vm.providers.collectAsStateWithLifecycle().value
            LaundryHomeScreen(
                providers = providers,
                onOpen = onOpen,
                onBack = { nav.popBackStack() }
            )
        }

        composable(LaundryDestinations.VIEW_ORDERS) {
            val vm = graphViewModel(nav)                  // <-- same graph VM
            val orders = vm.orders.collectAsStateWithLifecycle().value
            ViewOrderScreen(orders = orders, onBack = { nav.popBackStack() })
        }

        composable(LaundryDestinations.FILTER) {
            FilterScreen(onOpen = onOpen, onBack = { nav.popBackStack() } )
        }

        composable(
            route = LaundryDestinations.DETAILS,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStack ->
            val vm = graphViewModel(nav)
            val id = backStack.arguments?.getString("id") ?: return@composable
            val provider = vm.providers.value.firstOrNull { providerKey(it) == id }

            if (provider == null) {
                LaunchedEffect(Unit) { nav.popBackStack() }
            } else {
                ProviderDetailScreen(
                    provider = provider,
                    onBack = { nav.popBackStack() },
                    onOpen = onOpen
                )
            }
        }

        composable(LaundryDestinations.MAPSCREEN) {
            LaundryMapScreen(onOpen = onOpen, onBack = { nav.popBackStack() } )
        }

        composable(
            route = LaundryDestinations.SCHEDULE,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStack ->
            val vm = graphViewModel(nav)
            val id = backStack.arguments?.getString("id") ?: return@composable
            val provider = vm.providers.value.firstOrNull { providerKey(it) == id }

            if (provider == null) {
                LaunchedEffect(Unit) { nav.popBackStack() }
            } else {
                ScheduleScreen(
                    provider = provider,
                    providerName = provider.name,
                    providerPhotoUrl = provider.photoUrl,
                    initialDate = LocalDate.now(),
                    onBack = { nav.popBackStack() },
                    onOpen = onOpen
                )
            }
        }

        composable(
            route = LaundryDestinations.YOURLAUNDRY,
            arguments = listOf(
                navArgument("id")   { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
                navArgument("time") { type = NavType.StringType }
            )
        ) { backStack ->
            val vm   = graphViewModel(nav)
            val id   = backStack.arguments?.getString("id") ?: return@composable
            val date = backStack.arguments?.getString("date") ?: ""
            val time = backStack.arguments?.getString("time") ?: ""
            val provider = vm.providers.value.firstOrNull { providerKey(it) == id }

            if (provider == null) {
                LaunchedEffect(Unit) { nav.popBackStack() }
            } else {
                // We still use onOpen to navigate.
                // We also store LaundryOptions into SavedStateHandle so ORDER can read them.
                YourLaundryScreen(
                    provider = provider,
                    selectedDate = date,
                    selectedTime = time,
                    onBack = { nav.popBackStack() },
                    onOpen = { route -> onOpen(route) },
                    onConfirmOptions = { opts ->
                        nav.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set(LaundryDestinations.OPTS_KEY, opts)

                        onOpen(
                            LaundryDestinations.order(
                                id = providerKey(provider),
                                date = date,
                                time = time
                            )
                        )
                    }
                )
            }
        }

        composable(
            route = LaundryDestinations.ORDER,
            arguments = listOf(
                navArgument("id")   { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
                navArgument("time") { type = NavType.StringType }
            )
        ) { backStack ->
            val vm = graphViewModel(nav)
            val id   = backStack.arguments?.getString("id") ?: return@composable
            val date = backStack.arguments?.getString("date") ?: ""
            val time = backStack.arguments?.getString("time") ?: ""
            val provider = vm.providers.value.firstOrNull { providerKey(it) == id }

            // get the LaundryOptions from previous screen
            val opts: LaundryOptions? =
                nav.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<LaundryOptions>(LaundryDestinations.OPTS_KEY)

            if (provider == null) {
                LaunchedEffect(Unit) { nav.popBackStack() }
            } else {
                val items = buildList {
                    val kg = opts?.kg ?: 0
                    if (kg > 0) add(OrderItem("Cleaning", unitPrice = 15.0, unitSuffix = "/kg", qty = kg))

                    val dry = opts?.dryCount ?: 0
                    if (dry > 0) add(OrderItem("Dry cleaning", unitPrice = 10.0, unitSuffix = "", qty = dry))

                    if (opts?.ironing == true && kg > 0) {
                        add(OrderItem("Ironing", unitPrice = 3.0, unitSuffix = "/kg", qty = kg))
                    }
                }

                val order = Order(
                    providerName = provider.name,
                    providerPhotoUrl = provider.photoUrl,
                    dateLabel = "$date - $time",
                    addressLine1 = provider.address.substringBefore(",").trim(),
                    addressLine2 = provider.address.substringAfter(",", "").trim(),
                    items = items,
                    deliveryFee = 0.0,
                    options = opts,
                    totalVal = 0.0,
                )

                OrderScreen(
                    order = order,
                    onOpen=onOpen,
                    laundryViewModel = vm,
                    onBack = { nav.popBackStack() }
                )
            }
        }
    }
}
