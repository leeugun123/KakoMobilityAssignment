package com.example.kakomobilityassignment.data.api

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("origin")
    val origin : String ,
    @SerializedName("destination")
    val destination : String
)

data class LocationListResponse(
    @SerializedName("locations")
    val locationList : List<Location>
)

data class LocationPath(
    @SerializedName("points")
    val points : String,
    @SerializedName("traffic_state")
    val trafficState : String
)

data class LocationTimeDistanceResponse(
    @SerializedName("distance")
    val distance : Int,
    @SerializedName("time")
    val time : Int
)

data class PathFailResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("errorMessage")
    val errorMessage: String
)







