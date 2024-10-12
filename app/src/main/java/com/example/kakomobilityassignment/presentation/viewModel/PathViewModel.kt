package com.example.kakomobilityassignment.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakomobilityassignment.data.LocationPath
import com.example.kakomobilityassignment.data.LocationTimeDistanceResponse
import com.example.kakomobilityassignment.data.repository.KakaoMobilityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PathViewModel : ViewModel() {

    private val repository = KakaoMobilityRepository()

    private val _locationPathList = MutableStateFlow<List<LocationPath>>(emptyList())
    val locationPathList : StateFlow<List<LocationPath>> get() = _locationPathList

    private val _locationPathListErrorMessage = MutableStateFlow<String?>(null)
    val locationPathListErrorMessage: StateFlow<String?> get() = _locationPathListErrorMessage

    private val _locationTimeDistance = MutableStateFlow(LocationTimeDistanceResponse(0, 0))
    val locationTimeDistance: StateFlow<LocationTimeDistanceResponse> get() = _locationTimeDistance

    fun fetchLocationPathList(origin: String, destination: String) {
        viewModelScope.launch {
            repository.getLocationPath(
                requestedOrigin = origin,
                requestedDestination = destination,
                onSuccess = { locationPathList ->
                    _locationPathList.value = locationPathList
                },
                onError = { throwable ->
                    _locationPathListErrorMessage.value = throwable.message
                }
            )
        }
    }

    fun fetchLocationTimeDistance(origin: String, destination: String) {
        viewModelScope.launch {
            repository.getLocationTimeDistance(
                requestedOrigin = origin,
                requestedDestination = destination,
                onSuccess = { locationTimeDistance ->
                    _locationTimeDistance.value = locationTimeDistance
                },
                onError = {}
            )
        }
    }
}