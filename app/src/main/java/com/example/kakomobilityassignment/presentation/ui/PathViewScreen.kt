package com.example.kakomobilityassignment.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry

@Composable
fun PathViewScreen(navBackStackEntry: NavBackStackEntry) {

    val origin = navBackStackEntry.arguments?.getString("origin") ?: "Unknown"
    val destination = navBackStackEntry.arguments?.getString("destination") ?: "Unknown"

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "$origin $destination")
    }
}