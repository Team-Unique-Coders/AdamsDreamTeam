package com.example.doctor.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.doctor.ui.DoctorOptionScreen
import com.example.doctor.ui.DoctorProviderListScreen
import com.example.doctor.ui.DoctorProviderProfileScreen
import com.example.doctor.ui.DoctorWelcomeScreen
import com.example.doctor.ui.FiltersState

private object DoctorRoutes {
    const val Welcome = "doctor_welcome"
    const val Option = "doctor_option"
    const val List = "doctor_list"
    const val Profile = "doctor_profile"

    const val Filters = "doctor_filters"
}

@Composable
fun DoctorEntry(externalNav: NavHostController? = null, onClose: () -> Unit) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = DoctorRoutes.Welcome) {
        composable(DoctorRoutes.Welcome) {
            DoctorWelcomeScreen(
                onLetsGo = { nav.navigate(DoctorRoutes.Option) },
                onBack = onClose
            )
        }
        composable(DoctorRoutes.Option){
            DoctorOptionScreen(
                onDiagnosis = { },
                onAppointment = { nav.navigate(DoctorRoutes.List) },
                onBack = onClose
            )
        }
        composable(DoctorRoutes.List) {
            val filters = nav.currentBackStackEntry
                ?.savedStateHandle
                ?.get<FiltersState>("filters")

            DoctorProviderListScreen(
                onBack = { nav.popBackStack() },
                onOpenProvider = { nav.navigate(DoctorRoutes.Profile) },
                onOpenFilters = { },
                filters = filters
            )
        }
        composable(DoctorRoutes.Profile) {
            DoctorProviderProfileScreen(
                onBack = { nav.popBackStack() },
                onTakeAppointment = {})
        }
        composable(DoctorRoutes.Filters) {

        }
    }

}