package com.example.covid_19info.data.model

import com.google.gson.annotations.SerializedName

data class VaccinatedInfo(
    @SerializedName("currentCount") val currentCount:String,
    @SerializedName("data") val data:List<VaccinatedData>,
    @SerializedName("matchCount") val matchCount:String,
    @SerializedName("page") val page:String,
    @SerializedName("perPage") val perPage:String,
    @SerializedName("totalCount") val totalCount:String
)
