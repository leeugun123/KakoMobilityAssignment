package com.example.kakomobilityassignment.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakomobilityassignment.data.Location
import com.example.kakomobilityassignment.data.repository.PlaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LocationListViewModel() : ViewModel() {

    private val repository = PlaceRepository()

    private val _locationList = MutableStateFlow<List<Location>>(emptyList())
    val locationList: StateFlow<List<Location>> get() = _locationList

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    init {
        fetchPlaces()
    }

    private fun fetchPlaces() {
        viewModelScope.launch {
            repository.getPlaces(
                onSuccess = { locationList ->
                    _locationList.value = locationList
                },
                onError = { throwable ->
                    _errorMessage.value = throwable.message
                }
            )
        }
    }
}