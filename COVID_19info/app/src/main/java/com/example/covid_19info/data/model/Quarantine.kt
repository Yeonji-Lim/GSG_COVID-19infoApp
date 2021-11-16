package com.example.covid_19info.data.model

import com.google.gson.annotations.SerializedName

/*
{
    "_id":"6188adad8c5d0f44bc689f0b",
    "region":"성동구",
    "visitedDate":"2021-11-03T15:00:00.000Z",
    "latlng":"37.5402180709978, 127.05412659286698",
    "address":"서울 성동구 성수동2가 333-13",
    "placeEnglish":"",
    "place":"동부경찰관기동대",
    "createDate":"2021-11-08T13:55:09.000Z",
    "__v":0
}
*/

data class Quarantine(
    @SerializedName("_id") val _id:String,
    @SerializedName("region") val region:String,
    @SerializedName("visitedDate") val visitedDate:String,
    @SerializedName("latlng") val latlng:String,
    @SerializedName("address") val address:String,
    @SerializedName("placeEnglish") val placeEnglish:String,
    @SerializedName("place") val place:String,
    @SerializedName("createDate") val createDate:String,
    @SerializedName("__v") val __v:String
)
