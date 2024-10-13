package com.example.kakomobilityassignment.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakomobilityassignment.data.Location
import com.example.kakomobilityassignment.data.repository.KakaoMobilityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LocationListViewModel() : ViewModel() {

    private val repository = KakaoMobilityRepository()

    private val _locationList = MutableStateFlow<List<Location>>(emptyList())
    val locationList: StateFlow<List<Location>> get() = _locationList

    init { fetchPlaces() }

    private fun fetchPlaces() {
        viewModelScope.launch {
            repository.getLocationNameList(
                onSuccess = { locationList ->
                    _locationList.value = locationList
                }
            )
        }
    }
}