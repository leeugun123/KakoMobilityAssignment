package com.example.kakomobilityassignment.presentation.ui

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kakomobilityassignment.AssignLatLng
import com.example.kakomobilityassignment.R
import com.example.kakomobilityassignment.presentation.KakaoMobilityScreenTemplate
import com.example.kakomobilityassignment.presentation.common.LoadDataFailScreen
import com.example.kakomobilityassignment.presentation.common.LoadingScreen
import com.example.kakomobilityassignment.presentation.viewModel.PathViewModel
import com.example.kakomobilityassignment.ui.theme.TimeDistanceBoxColor
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.LatLngBounds
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import kotlinx.coroutines.delay


@Composable
fun PathViewScreen(
    pathViewModel: PathViewModel = hiltViewModel(),
    origin: String,
    destination: String
) {
    val locationPathList by pathViewModel.locationPathList.collectAsStateWithLifecycle()
    val locationPathListErrorCode by pathViewModel.locationPathListErrorCode.collectAsStateWithLifecycle()

    val locationTimeDistance by pathViewModel.locationTimeDistance.collectAsStateWithLifecycle()

    val trafficStateList: MutableList<String> = remember { mutableListOf() }
    val assignLatLngList: MutableList<MutableList<AssignLatLng>> = remember { mutableListOf() }

    var isDataLoaded by remember { mutableStateOf(false) }
    var isFailLoadData by remember { mutableStateOf(false) }

    pathViewModel.fetchLocationTimeDistance(origin = origin, destination = destination)
    pathViewModel.fetchLocationPathList(origin = origin, destination = destination)

    LaunchedEffect(Unit) {
        delay(2000L)
        if (locationPathList.isEmpty())
            isFailLoadData = true
    }

    LaunchedEffect(locationPathList) {

        if (locationPathList.isEmpty())
            return@LaunchedEffect

        locationPathList.forEach { locationPath ->

            val tempList: MutableList<AssignLatLng> = mutableListOf()

            locationPath.points.split(" ")
                .map { pair ->
                    val (longitude, latitude) = pair.split(",")
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

    KakaoMobilityScreenTemplate(screenContent = {
        if (isDataLoaded) {
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
        } else if (isFailLoadData) {

            val context = LocalContext.current

            var errorMessage = when (locationPathListErrorCode) {
                400 -> context.getString(R.string.error_400_message)
                401 -> context.getString(R.string.error_401_message)
                403 -> context.getString(R.string.error_403_message)
                404 -> context.getString(R.string.error_404_message)
                500 -> context.getString(R.string.error_500_message)
                else -> "알 수 없는 오류가 발생했습니다.\n 오류 코드: $locationPathListErrorCode"
            }

            if (locationPathListErrorCode == 0)
                errorMessage = stringResource(id = R.string.internet_connect_fail_message)

            LoadDataFailScreen(
                place = "$origin ~ $destination",
                code = locationPathListErrorCode,
                errorMessage = errorMessage
            )

        } else
            LoadingScreen()
    })
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
            Text(text = stringResource(id = R.string.time) + " " + time, color = Color.Yellow)
            Text(text = stringResource(id = R.string.distance) + " " + distance, color = Color.Yellow)
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

                        /* 출발, 도착 좌표 지정 */
                        val departMarkerLatLng = AssignLatLng(
                            latitude = assignLatLngList.firstOrNull()?.firstOrNull()?.latitude
                                ?: 0.0,
                            longitude = assignLatLngList.firstOrNull()?.firstOrNull()?.longitude
                                ?: 0.0
                        )

                        val arriveMarkerLatLng = AssignLatLng(
                            latitude = assignLatLngList.lastOrNull()?.lastOrNull()?.latitude ?: 0.0,
                            longitude = assignLatLngList.lastOrNull()?.lastOrNull()?.longitude
                                ?: 0.0
                        )

                        /* 카메라 경로 범위 이동 설정 */
                        val bounds = LatLngBounds(
                            LatLng.from(departMarkerLatLng.latitude, departMarkerLatLng.longitude),
                            LatLng.from(arriveMarkerLatLng.latitude, arriveMarkerLatLng.longitude)
                        )
                        val cameraUpdate = CameraUpdateFactory.fitMapPoints(bounds, 200)
                        map.moveCamera(cameraUpdate)

                        /* 출발 , 도착 마커 카카오 맵 표기 */
                        val departMarkerStyles: LabelStyles? = map.labelManager
                            ?.addLabelStyles(LabelStyles.from(LabelStyle.from(com.example.kakomobilityassignment.R.drawable.depart_img)))

                        val arriveMarkerStyles: LabelStyles? = map.labelManager
                            ?.addLabelStyles(LabelStyles.from(LabelStyle.from(com.example.kakomobilityassignment.R.drawable.arrive_img)))

                        val departOptions =
                            LabelOptions.from(
                                LatLng.from(
                                    departMarkerLatLng.latitude,
                                    departMarkerLatLng.longitude
                                )
                            ).setStyles(departMarkerStyles)

                        val arriveOptions =
                            LabelOptions.from(
                                LatLng.from(
                                    arriveMarkerLatLng.latitude,
                                    arriveMarkerLatLng.longitude
                                )
                            ).setStyles(arriveMarkerStyles)

                        map.labelManager?.layer?.addLabel(departOptions)
                        map.labelManager?.layer?.addLabel(arriveOptions)

                        /* 도로 색상 표시 */
                        val segmentList: List<RouteLineSegment> =
                            assignLatLngList.mapIndexed { idx, assignLatLng ->

                                val style = when (trafficStateList.getOrNull(idx)) {
                                    "UNKNOWN" -> RouteLineStyle.from(
                                        context,
                                        com.example.kakomobilityassignment.R.style.RouteUnknownLineStyle
                                    )

                                    "JAM" -> RouteLineStyle.from(
                                        context,
                                        R.style.RouteJamLineStyle
                                    )

                                    "DELAY" -> RouteLineStyle.from(
                                        context,
                                        R.style.RouteDelayLineStyle
                                    )

                                    "SLOW" -> RouteLineStyle.from(
                                        context,
                                        R.style.RouteSlowLineStyle
                                    )

                                    "NORMAL" -> RouteLineStyle.from(
                                        context,
                                        R.style.RouteNormalLineStyle
                                    )

                                    "BLOCK" -> RouteLineStyle.from(
                                        context,
                                        R.style.RouteBlockLineStyle
                                    )

                                    else -> RouteLineStyle.from(
                                        context,
                                        R.style.RouteUnknownLineStyle
                                    )
                                }

                                val routePoints = assignLatLng.map { latLng ->
                                    LatLng.from(latLng.latitude, latLng.longitude)
                                }

                                RouteLineSegment.from(routePoints, style)
                            }

                        val routeLineOptions = RouteLineOptions.from(segmentList)
                        map.routeLineManager?.layer?.addRouteLine(routeLineOptions)
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
