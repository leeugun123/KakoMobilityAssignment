package com.example.kakomobilityassignment.data.repository

import android.util.Log
import com.example.kakomobilityassignment.data.Location
import com.example.kakomobilityassignment.data.LocationResponse
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
        mobilityApi.getLocations().enqueue(object : Callback<LocationResponse> {
            override fun onResponse(
                call: Call<LocationResponse>,
                response: Response<LocationResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("TAG","성공" + response.body()?.locationList?.size)
                    onSuccess(response.body()?.locationList ?: emptyList())
                } else {
                    Log.e("TAG","실패")
                    onError(Exception("Failed to load places"))
                }
            }

            override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                onError(t)
            }
        })
    }


}