package com.example.kakomobilityassignment.data

import com.example.kakomobilityassignment.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface MobilityApiService {
    @GET("coding-assignment/locations")
    fun getLocations(
        @Header("Authorization") authorizationKey: String = BuildConfig.MOBILITY_API_HEADER_KEY
    ): Call<LocationResponse>
}

