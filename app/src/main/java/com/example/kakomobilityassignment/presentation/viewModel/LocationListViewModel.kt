package com.example.kakomobilityassignment.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakomobilityassignment.data.api.Location
import com.example.kakomobilityassignment.data.repository.KakaoMobilityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationListViewModel @Inject constructor(
    private val kakaoMobilityRepository: KakaoMobilityRepository
) : ViewModel() {

    private val _locationList = MutableStateFlow<List<Location>>(emptyList())
    val locationList: StateFlow<List<Location>> get() = _locationList

    init { fetchPlaces() }

    private fun fetchPlaces() {
        viewModelScope.launch {
            kakaoMobilityRepository.getLocationNameList(
                onSuccess = { locationList ->
                    _locationList.value = locationList
                }
            )
        }
    }
}