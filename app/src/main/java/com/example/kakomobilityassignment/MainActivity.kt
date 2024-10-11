package com.example.kakomobilityassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.example.kakomobilityassignment.presentation.viewModel.LocationListViewModel
import com.example.kakomobilityassignment.ui.theme.KakoMobilityAssignmentTheme
import com.kakao.vectormap.KakaoMapSdk

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val locationListViewModel: LocationListViewModel by viewModels()

        setContent {
            KakoMobilityAssignmentTheme {
                NavigationViewController(locationListViewModel = locationListViewModel)
            }
        }
        KakaoMapSdk.init(this@MainActivity, BuildConfig.KAKAO_MAP_NATIVE_API_KEY)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KakoMobilityAssignmentTheme {
        // NavigationViewController()
    }
}