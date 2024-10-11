package com.example.kakomobilityassignment.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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