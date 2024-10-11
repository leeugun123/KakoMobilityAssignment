package com.example.kakomobilityassignment.presentation.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kakomobilityassignment.presentation.KakaoMobilityScreenTemplate
import com.example.kakomobilityassignment.presentation.viewModel.PathViewModel
import com.example.kakomobilityassignment.ui.theme.TimeDistanceBoxColor
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView

@Composable
fun PathViewScreen(
    pathViewModel: PathViewModel = PathViewModel(),
    origin: String,
    destination: String
) {
    val locationPathList by pathViewModel.locationPathList.collectAsStateWithLifecycle()
    val locationPathListErrorMessage by pathViewModel.locationPathListErrorMessage.collectAsStateWithLifecycle()

    val locationTimeDistance by pathViewModel.locationTimeDistance.collectAsStateWithLifecycle()
    val locationTimeErrorMessage by pathViewModel.locationTimeErrorMessage.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        pathViewModel.fetchLocationTimeDistance(origin = origin, destination = destination)
        pathViewModel.fetchLocationPathList(origin = origin, destination = destination)
    }

    KakaoMobilityScreenTemplate(screenContent = {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            KakaoMapScreen()
            TimeDistanceBox()
        }
    })
}

@Composable
fun TimeDistanceBox() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 20.dp, bottom = 20.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier
                .size(150.dp, 100.dp)
                .background(TimeDistanceBoxColor, shape = RoundedCornerShape(20.dp))
                .padding(start = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "시간 : 1시간 30분", color = Color.White)
            Text(text = "거리 : 3000m", color = Color.White)
        }
    }
}

@Composable
fun KakaoMapScreen() {
    AndroidView(
        factory = { context ->
            val mapView = MapView(context)
            mapView.start(object : MapLifeCycleCallback() {
                override fun onMapDestroy() {
                    Log.e("TAG", "onMapDestroy: ")
                }

                override fun onMapError(error: Exception) {
                    Log.e("TAG", "onMapError: ", error)
                }
            }, object : KakaoMapReadyCallback() {
                override fun onMapReady(map: KakaoMap) {

                }
            })
            mapView
        },
        modifier = Modifier.fillMaxSize()
    )
}