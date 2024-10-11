package com.example.kakomobilityassignment.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kakomobilityassignment.R
import com.example.kakomobilityassignment.data.Location
import com.example.kakomobilityassignment.presentation.KakaoMobilityScreenTemplate
import com.example.kakomobilityassignment.presentation.viewModel.LocationListViewModel
import com.example.kakomobilityassignment.ui.theme.ArrivePlaceColor
import com.example.kakomobilityassignment.ui.theme.DepartPlaceColor

@Composable
fun PlaceListViewScreen(
    locationListViewModel: LocationListViewModel,
    navigationToPathView: (origin: String, destination: String) -> Unit
) {
    val locationList by locationListViewModel.locationList.collectAsStateWithLifecycle()
    val errorMessage by locationListViewModel.errorMessage.collectAsStateWithLifecycle()

    KakaoMobilityScreenTemplate(screenContent = {
        if (errorMessage != null) {
            Text(text = "Error: $errorMessage", modifier = Modifier.padding(16.dp))
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(locationList) { locationInfo ->
                PlaceComponents(
                    locationInfo = locationInfo,
                    onLocationClick = navigationToPathView
                )
            }
        }
    })
}

@Composable
fun PlaceComponents(
    locationInfo: Location,
    onLocationClick: (origin: String, destination: String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable {
                onLocationClick.invoke(
                    locationInfo.origin,
                    locationInfo.destination
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            RowPlaceContent(
                locationGuideText = stringResource(id = R.string.depart_text),
                locationInfo.origin,
                DepartPlaceColor
            )
            RowPlaceContent(
                locationGuideText = stringResource(id = R.string.arrive_text),
                locationInfo.destination,
                ArrivePlaceColor
            )
        }
    }
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp), color = Color.Gray
    )
}

@Composable
fun RowPlaceContent(locationGuideText: String, locationName: String, locationColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = locationGuideText,
            fontSize = 15.sp,
            color = Color.Black,
            fontWeight = FontWeight(500)
        )

        Text(
            text = locationName,
            fontSize = 15.sp,
            fontWeight = FontWeight(500),
            color = locationColor,
        )
    }
}











