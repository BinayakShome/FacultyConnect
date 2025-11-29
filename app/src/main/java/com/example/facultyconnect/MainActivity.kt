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
        enableEdgeToEdge()

        setContent {
            FacultyConnectTheme {
                val navController = rememberNavController()
                // create the ViewModel once, tied to the Activity
                val facultyViewModel: FacultyViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = "main"
                ) {
                    // Pass the SAME ViewModel instance into the nav graph
                    mainNavGraph(navController, facultyViewModel)
                }
            }
        }
    }
}
