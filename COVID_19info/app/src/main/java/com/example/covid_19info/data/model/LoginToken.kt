package com.example.covid_19info.data.model

import com.google.gson.annotations.SerializedName

data class LoginToken(
    @SerializedName("token") val token: String?
)
