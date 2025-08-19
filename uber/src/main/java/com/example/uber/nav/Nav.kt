package com.example.uber.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.uber.ui.DestinationBottomSheet
import com.example.uber.ui.MapWithBottomContent
import com.example.uber.ui.PickupBottomSheet

import com.example.uber.ui.RideFlowScreen
import org.osmdroid.util.GeoPoint

@Composable
fun UberNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.RIDE_FLOW
    ) {
        composable(Routes.RIDE_FLOW) {
            RideFlowScreen()
        }
        composable(Routes.MAP) {
            MapWithBottomContent(
                pickup = GeoPoint(33.7488, -84.3885),
                destination = GeoPoint(33.7488, -84.3885)
            ) {
                DestinationBottomSheet { dest ->
                    navController.popBackStack()
                }
            }
        }
        composable(Routes.PICKUP) {
            MapWithBottomContent(
                pickup = GeoPoint(33.7488, -84.3885), // or null if not set
                destination = GeoPoint(33.7542, -84.3900)
            ) {
                PickupBottomSheet { pickup ->
                    // Handle pickup selection
                    navController.popBackStack()
                }
            }
        }
    }
}
