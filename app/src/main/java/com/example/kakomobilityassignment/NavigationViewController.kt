package com.example.kakomobilityassignment

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kakomobilityassignment.presentation.ui.PathViewScreen
import com.example.kakomobilityassignment.presentation.ui.PlaceListViewScreen
import com.example.kakomobilityassignment.presentation.viewModel.LocationListViewModel

@Composable
fun NavigationViewController(locationListViewModel: LocationListViewModel) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "PlaceListViewScreen") {
        composable("PlaceListViewScreen") {
            PlaceListViewScreen(
                locationListViewModel = locationListViewModel,
                navigationToPathView = { origin, destination ->
                    navController.navigate(
                        "PathViewScreen/${origin}/${destination}"
                    )
                })
        }
        composable("PathViewScreen/{origin}/{destination}") { backStackEntry ->
            val locationArguments = backStackEntry.arguments
            PathViewScreen(
                origin = locationArguments?.getString("origin") ?: "Unknown Place",
                destination = locationArguments?.getString("destination") ?: "Unknown Place"
            )
        }
    }
}