package com.example.laundry.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.laundry.components.MapsCompo
import com.example.laundry.screens.FilterScreen
import com.example.laundry.screens.LaundryHomeScreen
import com.example.laundry.screens.LaundryMapScreen
import com.example.laundry.screens.LaundryScreen
import com.example.laundry.screens.Order
import com.example.laundry.screens.OrderItem
import com.example.laundry.screens.OrderScreen
import com.example.laundry.screens.ProviderDetailScreen
import com.example.laundry.screens.ScheduleScreen
import com.example.laundry.screens.YourLaundryScreen
import java.time.LocalDate

/* Nested graph routes */
object LaundryDestinations {
    const val GRAPH = "laundry"        // nested graph route
    const val LIST  = "laundry/list"   // start destination -> LaundryScreen
    const val HOME = "laundry/home"
    const val FILTER = "laundry/filter"
    const val DETAILSCREEN = "laundry/details"
    const val MAPSCREEN = "laundry/maps"
    const val SCHEDULESCREEN = "laundry/schedule"
    const val YOURLAUNDRY = "laundry/your"

    const val ORDERLAUNDRY = "laundry/order"

}

/**
 * Register the Laundry nested graph.
 * Accept an onOpen callback so Laundry screens can navigate:
 * onOpen = { route -> nav.navigate(route) }
 */
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
            LaundryHomeScreen(onOpen = onOpen, onBack = { nav.popBackStack() } )
        }
        composable(LaundryDestinations.FILTER) {
            FilterScreen(onOpen = onOpen, onBack = { nav.popBackStack() } )
        }
        composable(LaundryDestinations.DETAILSCREEN) {
            ProviderDetailScreen(onOpen = onOpen, onBack = { nav.popBackStack() } )
        }
        composable(LaundryDestinations.MAPSCREEN) {
            LaundryMapScreen(onOpen = onOpen, onBack = { nav.popBackStack() } )
        }
        composable(LaundryDestinations.SCHEDULESCREEN) {
            ScheduleScreen(onOpen = onOpen, onBack = { nav.popBackStack() } ,  providerName = "Jenny Jones",
                providerPhotoUrl = "https://images.pexels.com/photos/532220/pexels-photo-532220.jpeg",
                initialDate = LocalDate.of(2019, 3, 6) // matches your mock
            )
        }
        composable(LaundryDestinations.YOURLAUNDRY) {
            YourLaundryScreen(onOpen = onOpen, onBack = { nav.popBackStack() } )
        }

        composable(LaundryDestinations.ORDERLAUNDRY) {
            val order = Order(
                providerName = "Jenny Jones",
                providerPhotoUrl = "https://images.pexels.com/photos/532220/pexels-photo-532220.jpeg",
                dateLabel = "20 March, Thu - 14h",
                addressLine1 = "28 Broad Street",
                addressLine2 = "Johannesburg",
                items = listOf(
                    OrderItem("Cleaning", unitPrice = 15.0, unitSuffix = "/kg", qty = 5),
                    OrderItem("Dry cleaning", unitPrice = 10.0, unitSuffix = "", qty = 2),
                    OrderItem("Ironing", unitPrice = 3.0, unitSuffix = "/kg", qty = 5)
                ),
                deliveryFee = 0.0
            )
            OrderScreen(order,onOpen = onOpen, onBack = { nav.popBackStack() } )
        }


    }
}

