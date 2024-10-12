package com.example.kakomobilityassignment.data

import com.example.kakomobilityassignment.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoMobilityApiService {
    @GET("coding-assignment/locations")
    fun getLocationsNameList(
        @Header("Authorization") authorizationKey: String = BuildConfig.MOBILITY_API_HEADER_KEY
    ): Call<LocationListResponse>

    @GET("coding-assignment/routes")
    fun getLocationPath(
        @Header("Authorization") authorizationKey: String = BuildConfig.MOBILITY_API_HEADER_KEY,
        @Query("origin") origin : String,
        @Query("destination") destination : String,
    ) : Call<List<LocationPath>>

    @GET("coding-assignment/distance-time")
    fun getLocationTimeDistance(
        @Header("Authorization") authorizationKey: String = BuildConfig.MOBILITY_API_HEADER_KEY,
        @Query("origin") origin : String,
        @Query("destination") destination : String,
    ) : Call<LocationTimeDistanceResponse>

    @GET("coding-assignment/routes")
    fun getFailLoadPath(
        @Query("origin") origin : String,
        @Query("destination") destination : String,
    ) : Call<PathFailResponse>


}

