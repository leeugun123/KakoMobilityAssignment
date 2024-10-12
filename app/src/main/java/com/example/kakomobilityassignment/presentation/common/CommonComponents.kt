package com.example.kakomobilityassignment.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
fun LoadDataFailScreen(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "데이터 로드 실패")
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