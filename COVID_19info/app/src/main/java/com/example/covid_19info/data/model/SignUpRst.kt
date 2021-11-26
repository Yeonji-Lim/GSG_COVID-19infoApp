package com.example.covid_19info.data.model

import com.google.gson.annotations.SerializedName

data class SignUpRst(
    @SerializedName("email") val email:String,
    @SerializedName("password") val password:String?,
    @SerializedName("first_name") val first_name:String,
    @SerializedName("last_name") val last_name:String
)
