package com.example.facultyconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.facultyconnect.navigation.mainNavGraph
import com.example.facultyconnect.ui.theme.FacultyConnectTheme
import com.example.facultyconnect.vm.FacultyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Optional, for full screen layout

        setContent {
            FacultyConnectTheme {
                // Provide a NavController
                val navController = rememberNavController()

                // Optional: Provide your FacultyViewModel
                val facultyViewModel: FacultyViewModel = viewModel()

                // Set up the NavHost
                NavHost(
                    navController = navController,
                    startDestination = "main"
                ) {
                    // Main Navigation Graph
                    mainNavGraph(navController)
                }
            }
        }
    }
}
