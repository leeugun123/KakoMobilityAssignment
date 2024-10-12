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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kakomobilityassignment.AssignLatLng
import com.example.kakomobilityassignment.R
import com.example.kakomobilityassignment.presentation.KakaoMobilityScreenTemplate
import com.example.kakomobilityassignment.presentation.viewModel.PathViewModel
import com.example.kakomobilityassignment.ui.theme.TimeDistanceBoxColor
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import kotlinx.coroutines.delay


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

    val trafficStateList: MutableList<String> = remember { mutableListOf() }
    val assignLatLngList: MutableList<MutableList<AssignLatLng>> = remember { mutableListOf() }

    var isDataLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {

        pathViewModel.fetchLocationTimeDistance(origin = origin, destination = destination)
        pathViewModel.fetchLocationPathList(origin = origin, destination = destination)

        delay(1000L)

        locationPathList.forEach { locationPath ->

            val tempList: MutableList<AssignLatLng> = mutableListOf()

            locationPath.points.split(" ")
                .map { pair ->
                    val (latitude, longitude) = pair.split(",")
                    tempList.add(
                        AssignLatLng(
                            latitude = latitude.toDouble(),
                            longitude = longitude.toDouble()
                        )
                    )
                }

            assignLatLngList.add(tempList)
            trafficStateList.add(locationPath.trafficState)
        }

        isDataLoaded = true
    }


    if (isDataLoaded) {
        KakaoMobilityScreenTemplate(screenContent = {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                KakaoMapScreen(
                    assignLatLngList = assignLatLngList,
                    trafficStateList = trafficStateList
                )
                TimeDistanceBox(
                    time = convertSecondsToTimeString(locationTimeDistance.time),
                    distance = formatNumberWithCommasAndMinute(locationTimeDistance.distance)
                )
            }
        })
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "로딩중")
        }
    }

}

@Composable
fun TimeDistanceBox(time: String, distance: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 20.dp, bottom = 30.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier
                .size(150.dp, 100.dp)
                .background(TimeDistanceBoxColor, shape = RoundedCornerShape(10.dp))
                .padding(start = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "시간 : $time", color = Color.Yellow)
            Text(text = "거리 : $distance", color = Color.Yellow)
        }
    }
}

@Composable
private fun KakaoMapScreen(
    assignLatLngList: MutableList<MutableList<AssignLatLng>>,
    trafficStateList: MutableList<String>
) {
    AndroidView(
        factory = { context ->
            val mapView = MapView(context)

            mapView.start(
                object : MapLifeCycleCallback() {
                    override fun onMapDestroy() {}
                    override fun onMapError(error: Exception) {}
                },
                object : KakaoMapReadyCallback() {
                    override fun onMapReady(map: KakaoMap) {
                        val segmentList: List<RouteLineSegment> = assignLatLngList.mapIndexed { idx, assignLatLng ->

                            val style = when (trafficStateList.getOrNull(idx)) {
                                "UNKNOWN" -> RouteLineStyle.from(context, R.style.RouteUnknownLineStyle)
                                "JAM" -> RouteLineStyle.from(context, R.style.RouteJamLineStyle)
                                "DELAY" -> RouteLineStyle.from(context, R.style.RouteDelayLineStyle)
                                "SLOW" -> RouteLineStyle.from(context, R.style.RouteSlowLineStyle)
                                "NORMAL" -> RouteLineStyle.from(context, R.style.RouteNormalLineStyle)
                                "BLOCK" -> RouteLineStyle.from(context, R.style.RouteBlockLineStyle)
                                else -> RouteLineStyle.from(context, R.style.RouteUnknownLineStyle)
                            }

                            val routePoints = assignLatLng.map { e ->
                                LatLng.from(e.longitude, e.latitude)
                            }

                            RouteLineSegment.from(routePoints, style)
                        }

                        val routeLineOptions = RouteLineOptions.from(segmentList)
                        map.routeLineManager?.layer?.addRouteLine(routeLineOptions)
                    }

                    override fun getZoomLevel(): Int {
                        return 10;
                    }
                },
            )
            mapView
        },
        modifier = Modifier.fillMaxSize()
    )
}

private fun convertSecondsToTimeString(seconds: Int): String {
    val hours = seconds / 3600
    val remainingSecondsAfterHours = seconds % 3600
    val minutes = remainingSecondsAfterHours / 60
    val remainingSeconds = remainingSecondsAfterHours % 60

    return buildString {
        if (hours > 0) append("${hours}시간 ")
        if (minutes > 0) append("${minutes}분 ")
        if (remainingSeconds > 0 || isEmpty()) append("${remainingSeconds}초")
    }.trim()
}

private fun formatNumberWithCommasAndMinute(number: Int) =
    "%,d".format(number) + "m"
