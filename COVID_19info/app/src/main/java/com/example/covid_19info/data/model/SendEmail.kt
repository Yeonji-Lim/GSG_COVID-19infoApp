package com.example.covid_19info.data.model

import com.google.gson.annotations.SerializedName

data class SendEmail (
    @SerializedName("email") val email: String
)