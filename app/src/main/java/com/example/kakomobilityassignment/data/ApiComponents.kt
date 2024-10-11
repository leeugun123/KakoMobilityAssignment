package com.example.kakomobilityassignment.data

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

data class LocationPathsResponse(
    val locationPathList : List<LocationPath>
)

data class LocationTimeDistanceResponse(
    @SerializedName("distance")
    val distance : Int,
    @SerializedName("time")
    val time : Int
)







