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
                    override fun onMapDestroy() {
                        Log.e("TAG", "onMapDestroy: ")
                    }

                    override fun onMapError(error: Exception) {
                        Log.e("TAG", "onMapError: ", error)
                    }
                },
                object : KakaoMapReadyCallback() {
                    override fun onMapReady(map: KakaoMap) {

                        val unknownRoutePoints = mutableListOf<LatLng>()
                        val blockRoutePoints = mutableListOf<LatLng>()
                        val jamRoutePoints = mutableListOf<LatLng>()
                        val delayRoutePoints = mutableListOf<LatLng>()
                        val slowRoutePoints = mutableListOf<LatLng>()
                        val normalRoutePoints = mutableListOf<LatLng>()

                        assignLatLngList.forEachIndexed { idx, assignLatLng ->

                            val trafficState = trafficStateList[idx]

                            when (trafficState) {

                                "UNKNOWN" -> {
                                    assignLatLng.forEach { e ->
                                        unknownRoutePoints.add(
                                            LatLng.from(
                                                e.longitude,
                                                e.latitude,
                                            )
                                        )
                                    }
                                }

                                "JAM" -> {
                                    assignLatLng.forEach { e ->
                                        jamRoutePoints.add(
                                            LatLng.from(
                                                e.longitude,
                                                e.latitude,
                                            )
                                        )
                                    }
                                }

                                "DELAY" -> {
                                    assignLatLng.forEach { e ->
                                        delayRoutePoints.add(
                                            LatLng.from(
                                                e.longitude,
                                                e.latitude,
                                            )
                                        )
                                    }
                                }

                                "SLOW" -> {
                                    assignLatLng.forEach { e ->
                                        slowRoutePoints.add(
                                            LatLng.from(
                                                e.longitude,
                                                e.latitude,
                                            )
                                        )
                                    }
                                }

                                "NORMAL" -> {
                                    assignLatLng.forEach { e ->
                                        normalRoutePoints.add(
                                            LatLng.from(
                                                e.longitude,
                                                e.latitude,
                                            )
                                        )
                                    }
                                }

                                "BLOCK" -> {
                                    assignLatLng.forEach { e ->
                                        blockRoutePoints.add(
                                            LatLng.from(
                                                e.longitude,
                                                e.latitude,
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        val unknownStyle =
                            RouteLineStyle.from(context, R.style.RouteUnknownLineStyle)
                        val blockStyle = RouteLineStyle.from(context, R.style.RouteBlockLineStyle)
                        val jamStyle = RouteLineStyle.from(context, R.style.RouteJamLineStyle)
                        val delayStyle = RouteLineStyle.from(context, R.style.RouteDelayLineStyle)
                        val slowStyle = RouteLineStyle.from(context, R.style.RouteSlowLineStyle)
                        val normalStyle = RouteLineStyle.from(context, R.style.RouteNormalLineStyle)

                        val unknownSegment = RouteLineSegment.from(unknownRoutePoints, unknownStyle)
                        val blockSegment = RouteLineSegment.from(blockRoutePoints, blockStyle)
                        val jamSegment = RouteLineSegment.from(jamRoutePoints, jamStyle)
                        val delaySegment = RouteLineSegment.from(delayRoutePoints, delayStyle)
                        val slowSegment = RouteLineSegment.from(slowRoutePoints, slowStyle)
                        val normalSegment = RouteLineSegment.from(normalRoutePoints, normalStyle)

                        val routeLineOptions = RouteLineOptions.from(
                            listOf(
                                unknownSegment, slowSegment,
                                normalSegment, delaySegment, jamSegment
                            )
                        )

                        map.routeLineManager!!.layer.addRouteLine(routeLineOptions)
                    }

                    /*
                        override fun getPosition(): LatLng {}
                        // 시작할때 좌표 설정
                    */

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
