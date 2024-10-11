package com.example.kakomobilityassignment.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakomobilityassignment.data.LocationTimeDistanceResponse
import com.example.kakomobilityassignment.data.repository.PlaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PathViewModel : ViewModel() {

    private val repository = PlaceRepository()

    private val _locationTimeDistance = MutableStateFlow(LocationTimeDistanceResponse(0, 0))
    val locationTimeDistance: StateFlow<LocationTimeDistanceResponse> get() = _locationTimeDistance

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun fetchLocationTimeDistance(origin: String, destination: String) {
        viewModelScope.launch {
            repository.getPlaceTimeDistance(
                origin = origin,
                destination = destination,
                onSuccess = { locationTimeDistance ->
                    _locationTimeDistance.value = locationTimeDistance
                },
                onError = { throwable ->
                    _errorMessage.value = throwable.message
                }
            )
        }
    }

}