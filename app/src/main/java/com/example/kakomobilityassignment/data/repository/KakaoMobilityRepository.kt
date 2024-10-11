package com.example.kakomobilityassignment.data.repository

import android.util.Log
import com.example.kakomobilityassignment.data.KakaoMobilityApiService
import com.example.kakomobilityassignment.data.Location
import com.example.kakomobilityassignment.data.LocationListResponse
import com.example.kakomobilityassignment.data.LocationPath
import com.example.kakomobilityassignment.data.LocationPathsResponse
import com.example.kakomobilityassignment.data.LocationTimeDistanceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class KakaoMobilityRepository {

    private val kakaoMobilityApiService: KakaoMobilityApiService = Retrofit.Builder()
        .baseUrl("https://taxi-openapi.sandbox.onkakao.net/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(KakaoMobilityApiService::class.java)

    fun getLocationNameList(
        onSuccess: (List<Location>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        kakaoMobilityApiService.getLocationsNameList()
            .enqueue(object : Callback<LocationListResponse> {
                override fun onResponse(
                    call: Call<LocationListResponse>,
                    response: Response<LocationListResponse>
                ) {
                    if (response.isSuccessful) {
                        onSuccess(response.body()?.locationList ?: emptyList())
                    } else {
                        onError(Exception("Failed to load places"))
                    }
                }

                override fun onFailure(call: Call<LocationListResponse>, t: Throwable) {
                    onError(t)
                }
            })
    }

    fun getLocationPath(
        requestedOrigin: String,
        requestedDestination: String,
        onSuccess: (List<LocationPath>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        kakaoMobilityApiService.getLocationPath(
            origin = requestedOrigin,
            destination = requestedDestination
        )
            .enqueue(object : Callback<LocationPathsResponse> {
                override fun onResponse(
                    call: Call<LocationPathsResponse>,
                    response: Response<LocationPathsResponse>
                ) {
                    if (response.isSuccessful) {
                        onSuccess(response.body()?.locationPathList ?: emptyList())
                    } else {
                        onError(Exception("Failed to load places"))
                    }
                }

                override fun onFailure(call: Call<LocationPathsResponse>, t: Throwable) {
                    Log.e("TAG", t.toString())
                    onError(t)
                }
            })
    }

    fun getLocationTimeDistance(
        requestedOrigin: String,
        requestedDestination: String,
        onSuccess: (LocationTimeDistanceResponse) -> Unit,
        onError: (Throwable) -> Unit
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
                                locationTimeDistanceResponse.time,
                                locationTimeDistanceResponse.distance
                            )
                        )
                    } else {
                        onError(Exception("Failed to Location TimeDistance"))
                    }
                }

                override fun onFailure(call: Call<LocationTimeDistanceResponse>, t: Throwable) {
                    onError(t)
                }

            })
    }


}