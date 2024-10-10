package com.example.kakomobilityassignment

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kakomobilityassignment.presentation.ui.PathViewScreen
import com.example.kakomobilityassignment.presentation.ui.PlaceListViewScreen

@Composable
fun NavigationViewController() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "PlaceListViewScreen") {
        composable("PlaceListViewScreen") {
            PlaceListViewScreen(navigationToPathView = { origin, destination ->
                navController.navigate(
                    "PathViewScreen/${origin}/${destination}"
                )
            })
        }
        composable("PathViewScreen/{origin}/{destination}") { backStackEntry ->
            val arguments = backStackEntry.arguments
            PathViewScreen(
                origin = arguments?.getString("origin") ?: "Unknown Place",
                destination = arguments?.getString("destination") ?: "Unknown Place"
            )
        }
    }
}