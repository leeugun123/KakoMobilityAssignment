package com.example.kakomobilityassignment.data

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("origin")
    val origin : String ,
    @SerializedName("destination")
    val destination : String
)

data class LocationResponse(
    @SerializedName("locations")
    val locationList : List<Location>
)




