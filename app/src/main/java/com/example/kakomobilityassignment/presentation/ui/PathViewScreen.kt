package com.example.kakomobilityassignment.presentation.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.kakomobilityassignment.presentation.ScreenScaffoldTemplate
import com.example.kakomobilityassignment.presentation.viewModel.PathViewModel

@Composable
fun PathViewScreen(
    pathViewModel: PathViewModel = PathViewModel(),
    origin: String,
    destination: String
) {

    val locationPathList by pathViewModel.locationPathList.collectAsState()
    val locationPathListErrorMessage by pathViewModel.locationPathListErrorMessage.collectAsState()

    val locationTimeDistance by pathViewModel.locationTimeDistance.collectAsState()
    val locationTimeErrorMessage by pathViewModel.locationTimeErrorMessage.collectAsState()

    LaunchedEffect(Unit) {
        pathViewModel.fetchLocationTimeDistance(origin = origin, destination = destination)
        pathViewModel.fetchLocationPath(origin = origin, destination = destination)
    }

    ScreenScaffoldTemplate(screenContent = {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = locationTimeDistance.time.toString() + " " + locationTimeDistance.distance.toString())

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(locationPathList) { locationPath ->
                    Log.e("TAG",locationPath.points)
                    Log.e("TAG",locationPath.trafficState)
                }
            }

        }
    })
}