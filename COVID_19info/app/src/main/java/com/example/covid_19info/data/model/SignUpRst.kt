package com.example.covid_19info.data.model

import com.google.gson.annotations.SerializedName

data class SignUpRst(
    @SerializedName("email") val email:String,
    @SerializedName("password") val password:String?
)
