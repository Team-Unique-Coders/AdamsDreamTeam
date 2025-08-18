package com.example.tinder.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tinder.ui.AddProfilePicture
import com.example.tinder.ui.EnableGeolocation
import com.example.tinder.ui.ProfileFullScreen
import com.example.tinder.ui.RainEffectController
import com.example.tinder.ui.TakePhoto
import com.example.tinder.ui.Tutorial
import com.example.tinder.ui.WelcomeScreen
import com.example.tinder.ui.mainscreen.BoostScreen
import com.example.tinder.ui.mainscreen.CompleteProfile
import com.example.tinder.ui.mainscreen.LikeScreen
import com.example.tinder.ui.mainscreen.ModifyProfile
import com.example.tinder.ui.mainscreen.ProfileDetailScreen
import com.example.tinder.ui.mainscreen.SuperLikeScreen


@Composable
fun AppNavigation(
    navController: NavHostController,
    controller: RainEffectController
) {
    NavHost(navController = navController, startDestination = Routes.WELCOME) {

        composable(Routes.WELCOME) {
            WelcomeScreen(onNext = { navController.navigate(Routes.ADD_PROFILE) })
        }

        composable(Routes.ADD_PROFILE) {
            AddProfilePicture(
                onPhoto = { navController.navigate(Routes.TAKE_PHOTO) },
                onPicture = { navController.navigate(Routes.COMPLETE_PROFILE) }
            )
        }

        composable(Routes.TAKE_PHOTO) {
            TakePhoto(onPicture = { navController.navigate(Routes.COMPLETE_PROFILE) })
        }

        composable(Routes.COMPLETE_PROFILE) {
            CompleteProfile(
                onGeolocation = { navController.navigate(Routes.ENABLE_GEOLOCATION) },
                onProfileComplete = { navController.navigate(Routes.ENABLE_GEOLOCATION) }
            )
        }

        composable(Routes.ENABLE_GEOLOCATION) {
            EnableGeolocation(onTutorial = { navController.navigate(Routes.TUTORIAL) })
        }

        composable(Routes.TUTORIAL) {
            Tutorial(onMain = { navController.navigate(Routes.PROFILE_DETAIL) })
        }

        composable(Routes.PROFILE_DETAIL) {
            ProfileDetailScreen(
                controller = controller,
                navController = navController,
                onNext = {navController.navigate(Routes.MODIFY_PROFILE)},
            )
        }

        composable(Routes.BOOST_SCREEN) {
            BoostScreen(
                onNext = { navController.popBackStack() },
                onNoThanks = { navController.popBackStack() }
            )
        }

        composable(Routes.LIKE_SCREEN) {
            LikeScreen(
                onNext = { navController.popBackStack() },
                onNoThanks = { navController.popBackStack() }
            )
        }

        composable(Routes.SUPER_LIKE_SCREEN) {
            SuperLikeScreen(
                onNext = { navController.popBackStack() },
                onNoThanks = { navController.popBackStack() }
            )
        }
        composable(Routes.MODIFY_PROFILE) {
            ModifyProfile(onNext = { navController.popBackStack() })
        }
        composable(
            Routes.PROFILE_FULL,
            arguments = listOf(
                navArgument("photo") { type = NavType.IntType },
                navArgument("name") { type = NavType.StringType },
                navArgument("age") { type = NavType.IntType },
                navArgument("city") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            ProfileFullScreen(
                photo = args.getInt("photo"),
                name = args.getString("name")!!,
                age = args.getInt("age"),
                city = args.getString("city")!!,
                navController = navController
            )
        }
    }
}

