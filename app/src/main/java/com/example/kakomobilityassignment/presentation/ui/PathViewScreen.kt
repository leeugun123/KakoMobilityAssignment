package com.example.kakomobilityassignment.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.kakomobilityassignment.presentation.ScreenScaffoldTemplate
import com.example.kakomobilityassignment.presentation.viewModel.PathViewModel

@Composable
fun PathViewScreen(
    pathViewModel: PathViewModel = PathViewModel(),
    origin: String,
    destination: String
) {
    val locationTimeDistance by pathViewModel.locationTimeDistance.collectAsState()
    val errorMessage by pathViewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        pathViewModel.fetchLocationTimeDistance(origin = origin, destination = destination)
    }

    ScreenScaffoldTemplate(screenContent = {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "$origin $destination")
            Text(text = "${locationTimeDistance.time} ${locationTimeDistance.distance}")
            Text(text = errorMessage.toString())
        }
    })
}