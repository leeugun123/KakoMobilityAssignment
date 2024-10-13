package com.example.kakomobilityassignment.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kakomobilityassignment.R

@Composable
fun TitleBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.title_bar_content),
            color = Color.Yellow,
            fontSize = 20.sp
        )
    }
}

@Composable
fun LoadDataFailScreen(place: String = "", code: Int = 0, errorMessage: String = "") {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.fail_load_path_message),
            fontSize = 30.sp,
            fontWeight = FontWeight(800)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            LoadDataFailScreenRowBar(
                leftContent = stringResource(id = R.string.path),
                rightContent = place
            )
            LoadDataFailScreenRowBar(
                leftContent = stringResource(id = R.string.code),
                rightContent = code.toString()
            )
            LoadDataFailScreenRowBar(
                leftContent = stringResource(id = R.string.message),
                rightContent = errorMessage.ifEmpty { stringResource(id = R.string.empty_error_message) }
            )
        }
    }
}

@Composable
fun LoadDataFailScreenRowBar(leftContent: String, rightContent: String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 40.dp)) {
        Text(text = leftContent, fontSize = 25.sp)
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = rightContent, fontSize = 25.sp)
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            strokeWidth = 4.dp,
            modifier = Modifier.size(50.dp)
        )
    }
}