package com.example.facultyconnect.navigation

import android.annotation.SuppressLint
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.facultyconnect.views.DetailScreen
import com.example.facultyconnect.views.HomeScreen
import com.example.facultyconnect.vm.FacultyViewModel

@SuppressLint("UnrememberedGetBackStackEntry")
fun NavGraphBuilder.mainNavGraph(
    navController: NavController,
    facultyViewModel: FacultyViewModel
) {

    navigation(
        startDestination = Screen.HomeScreen.route,
        route = "main"
    ) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(
                navController = navController,
                facultyViewModel = facultyViewModel
            )
        }

        composable(
            route = "detail_screen/{facultyId}",
            arguments = listOf(navArgument("facultyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("facultyId") ?: ""
            DetailScreen(
                navController = navController,
                facultyId = id,
                facultyViewModel = facultyViewModel
            )
        }
    }
}