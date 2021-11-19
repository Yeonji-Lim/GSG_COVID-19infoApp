package com.example.covid_19info.data.model

import com.google.gson.annotations.SerializedName

data class Quarantines(
    @SerializedName("data") val data:List<Quarantine>
)
