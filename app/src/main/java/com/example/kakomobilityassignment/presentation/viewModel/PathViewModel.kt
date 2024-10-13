package com.example.kakomobilityassignment.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakomobilityassignment.data.LocationPath
import com.example.kakomobilityassignment.data.LocationTimeDistanceResponse
import com.example.kakomobilityassignment.data.repository.KakaoMobilityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PathViewModel @Inject constructor(
    private val kakaoMobilityRepository: KakaoMobilityRepository
) : ViewModel() {

    private val _locationPathList = MutableStateFlow<List<LocationPath>>(emptyList())
    val locationPathList : StateFlow<List<LocationPath>> get() = _locationPathList

    private val _locationPathListErrorCode = MutableStateFlow(0)
    val locationPathListErrorCode: StateFlow<Int> get() = _locationPathListErrorCode

    private val _locationTimeDistance = MutableStateFlow(LocationTimeDistanceResponse(0, 0))
    val locationTimeDistance: StateFlow<LocationTimeDistanceResponse> get() = _locationTimeDistance

    fun fetchLocationPathList(origin: String, destination: String) {
        viewModelScope.launch {
            kakaoMobilityRepository.getLocationPath(
                requestedOrigin = origin,
                requestedDestination = destination,
                onSuccess = { locationPathList ->
                    _locationPathList.value = locationPathList
                },
                onError = { errorCode ->
                    _locationPathListErrorCode.value = errorCode
                }
            )
        }
    }

    fun fetchLocationTimeDistance(origin: String, destination: String) {
        viewModelScope.launch {
            kakaoMobilityRepository.getLocationTimeDistance(
                requestedOrigin = origin,
                requestedDestination = destination,
                onSuccess = { locationTimeDistance ->
                    _locationTimeDistance.value = locationTimeDistance
                }
            )
        }
    }
}