package com.example.kakomobilityassignment.data

import com.example.kakomobilityassignment.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MobilityApiService {
    @GET("coding-assignment/locations")
    fun getLocationsList(
        @Header("Authorization") authorizationKey: String = BuildConfig.MOBILITY_API_HEADER_KEY
    ): Call<LocationListResponse>

    @GET("coding-assignment/distance-time")
    fun getLocationTimeDistance(
        @Header("Authorization") authorizationKey: String = BuildConfig.MOBILITY_API_HEADER_KEY,
        @Query("origin") origin : String,
        @Query("destination") destination : String,
    ) : Call<LocationTimeDistanceResponse>


}

