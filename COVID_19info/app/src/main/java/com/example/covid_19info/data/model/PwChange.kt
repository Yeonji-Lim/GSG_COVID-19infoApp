package com.example.covid_19info.data.model

import com.google.gson.annotations.SerializedName

data class PwChange (
    @SerializedName("email") val pw: String,
    @SerializedName("code") val code: String
)