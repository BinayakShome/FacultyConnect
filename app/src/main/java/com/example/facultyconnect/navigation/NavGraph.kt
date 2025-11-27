package com.example.facultyconnect.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.facultyconnect.views.DetailScreen
import com.example.facultyconnect.views.HomeScreen
import com.example.facultyconnect.vm.FacultyViewModel

fun NavGraphBuilder.mainNavGraph(navController: NavController) {

    navigation(
        startDestination = Screen.HomeScreen.route,
        route = "main"
    ) {

        composable(Screen.HomeScreen.route) {
            val facultyViewModel: FacultyViewModel = viewModel()
            HomeScreen(
                navController = navController,
                onFacultyClick = { facultyId ->
                    navController.navigate("detail_screen/$facultyId")
                },
                facultyViewModel = facultyViewModel
            )
        }

        composable(
            route = "detail_screen/{facultyId}",
            arguments = listOf(navArgument("facultyId") { type = NavType.StringType })
        ) {
            val facultyViewModel: FacultyViewModel = viewModel()
            DetailScreen(
                navController = navController,
                facultyId = it.arguments?.getString("facultyId") ?: "",
                facultyViewModel = facultyViewModel
            )
        }
    }
}