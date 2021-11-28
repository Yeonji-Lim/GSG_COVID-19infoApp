package com.example.covid_19info.data.model

import com.google.gson.annotations.SerializedName

data class PwChange (
    @SerializedName("code") val code: String,
    @SerializedName("password") val password: String
)