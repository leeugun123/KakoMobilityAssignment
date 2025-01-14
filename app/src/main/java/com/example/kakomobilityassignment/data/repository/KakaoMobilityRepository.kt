package com.example.kakomobilityassignment.data.repository

import com.example.kakomobilityassignment.data.api.KakaoMobilityApiService
import com.example.kakomobilityassignment.data.api.Location
import com.example.kakomobilityassignment.data.api.LocationListResponse
import com.example.kakomobilityassignment.data.api.LocationPath
import com.example.kakomobilityassignment.data.api.LocationTimeDistanceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class KakaoMobilityRepository @Inject constructor(
    private val kakaoMobilityApiService: KakaoMobilityApiService
) {

    fun getLocationNameList(
        onSuccess: (List<Location>) -> Unit,
    ) {
        kakaoMobilityApiService.getLocationsNameList()
            .enqueue(object : Callback<LocationListResponse> {
                override fun onResponse(
                    call: Call<LocationListResponse>,
                    response: Response<LocationListResponse>
                ) {
                    if (response.isSuccessful) {
                        onSuccess(response.body()?.locationList ?: emptyList())
                    }
                }

                override fun onFailure(call: Call<LocationListResponse>, t: Throwable) {}
            })
    }

    fun getLocationPath(
        requestedOrigin: String,
        requestedDestination: String,
        onSuccess: (List<LocationPath>) -> Unit,
        onError: (Int) -> Unit
    ) {
        kakaoMobilityApiService.getLocationPath(
            origin = requestedOrigin,
            destination = requestedDestination
        )
            .enqueue(object : Callback<List<LocationPath>> {
                override fun onResponse(
                    call: Call<List<LocationPath>>,
                    response: Response<List<LocationPath>>
                ) {
                    if (response.isSuccessful)
                        onSuccess(response.body() ?: emptyList())
                    else
                        onError(response.code())
                }

                override fun onFailure(call: Call<List<LocationPath>>, t: Throwable) {}
            })
    }

    fun getLocationTimeDistance(
        requestedOrigin: String,
        requestedDestination: String,
        onSuccess: (LocationTimeDistanceResponse) -> Unit
    ) {
        kakaoMobilityApiService.getLocationTimeDistance(
            origin = requestedOrigin,
            destination = requestedDestination
        )
            .enqueue(object : Callback<LocationTimeDistanceResponse> {
                override fun onResponse(
                    call: Call<LocationTimeDistanceResponse>,
                    response: Response<LocationTimeDistanceResponse>
                ) {
                    if (response.isSuccessful) {
                        val locationTimeDistanceResponse =
                            response.body() ?: LocationTimeDistanceResponse(0, 0)
                        onSuccess(
                            LocationTimeDistanceResponse(
                                distance = locationTimeDistanceResponse.distance,
                                time = locationTimeDistanceResponse.time
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<LocationTimeDistanceResponse>, t: Throwable) {}
            })
    }


}