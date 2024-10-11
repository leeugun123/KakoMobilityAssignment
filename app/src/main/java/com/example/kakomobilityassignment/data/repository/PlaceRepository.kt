package com.example.kakomobilityassignment.data.repository

import com.example.kakomobilityassignment.data.Location
import com.example.kakomobilityassignment.data.LocationListResponse
import com.example.kakomobilityassignment.data.LocationTimeDistanceResponse
import com.example.kakomobilityassignment.data.MobilityApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlaceRepository {

    private val mobilityApi: MobilityApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://taxi-openapi.sandbox.onkakao.net/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mobilityApi = retrofit.create(MobilityApiService::class.java)
    }

    fun getPlaces(
        onSuccess: (List<Location>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        mobilityApi.getLocationsList().enqueue(object : Callback<LocationListResponse> {
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

    fun getPlaceTimeDistance(
        origin : String,
        destination : String,
        onSuccess: (LocationTimeDistanceResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        mobilityApi.getLocationTimeDistance(origin = origin, destination = destination)
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