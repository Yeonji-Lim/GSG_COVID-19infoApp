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
//{
//    baseDate	string
//    통계 기준일자
//    sido	string
//    지역명칭
//    firstCnt	integer
//    당일 통계(1차 접종)
//    secondCnt	integer
//    당일 통계(2차 접종)
//    totalFirstCnt	integer
//    전체 누적 통계(1차 접종)
//    totalSecondCnt	integer
//    전체 누적 통계(2차 접종)
//    accumulatedFirstCnt	integer
//    전일까지의 누적 통계 (1차 접종)
//    accumulatedSecondCnt	integer
//    전일까지의 누적 통계 (2차 접종)
//    id	integer
//    thirdCnt	integer
//    당일 통계(3차 접종)
//    totalThirdCnt	integer
//    전체 누적 통계(3차 접종)
//    accumulatedThirdCnt	integer
//    전일까지의 누적 통계 (3차 접종)
//}