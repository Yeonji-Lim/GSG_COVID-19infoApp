package com.example.covid_19info.data.model

import com.google.gson.annotations.SerializedName
import java.sql.Date


//{
//    "accumulatedFirstCnt": 95915,
//    "accumulatedSecondCnt": 0,
//    "accumulatedThirdCnt": 0,
//    "baseDate": "2021-03-11 00:00:00",
//    "firstCnt": 9961,
//    "id": 10,
//    "secondCnt": 0,
//    "sido": "경기도",
//    "thirdCnt": 0,
//    "totalFirstCnt": 105876,
//    "totalSecondCnt": 0,
//    "totalThirdCnt": 0
//}
data class VaccinatedData (
    @SerializedName("accumulatedFirstCnt") val accumulatedFirstCnt:Int,
    @SerializedName("accumulatedSecondCnt") val accumulatedSecondCnt:Int,
    @SerializedName("accumulatedThirdCnt") val accumulatedThirdCnt:Int,
    @SerializedName("baseDate") val baseDate:Date,
    @SerializedName("firstCnt") val firstCnt:Int,
    @SerializedName("id") val id:Int,
    @SerializedName("secondCnt") val secondCnt:Int,
    @SerializedName("sido") val sido:String,
    @SerializedName("thirdCnt") val thirdCnt:Int,
    @SerializedName("totalFirstCnt") val totalFirstCnt:Int,
    @SerializedName("totalSecondCnt") val totalSecondCnt:Int,
    @SerializedName("totalThirdCnt") val totalThirdCnt:Int
)