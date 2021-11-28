package com.example.covid_19info.data.model

import com.google.gson.annotations.SerializedName
import java.util.*


data class ServerUserLocation(
    @SerializedName("id") val id: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longtitude: Double,
    @SerializedName("date") val date: String
)
