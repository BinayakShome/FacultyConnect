package com.example.facultyconnect.navigation

sealed class Screen(val route: String)
{
    object HomeScreen : Screen("home_screen")
    object DetailScreen : Screen("detail_screen/{facultyId}") {
        fun createRoute(facultyId: String) = "detail_screen/$facultyId"
    }
}