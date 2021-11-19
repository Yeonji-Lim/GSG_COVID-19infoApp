package com.example.covid_19info.data

import com.example.covid_19info.data.model.VaccinatedInfo
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

//
//{
//    "currentCount": 10,
//    "data": [
//    {
//        "accumulatedFirstCnt": 449535,
//        "accumulatedSecondCnt": 0,
//        "accumulatedThirdCnt": 0,
//        "baseDate": "2021-03-11 00:00:00",
//        "firstCnt": 51100,
//        "id": 1,
//        "secondCnt": 0,
//        "sido": "전국",
//        "thirdCnt": 0,
//        "totalFirstCnt": 500635,
//        "totalSecondCnt": 0,
//        "totalThirdCnt": 0
//    }
//    {
//        "accumulatedFirstCnt": 794,
//        "accumulatedSecondCnt": 0,
//        "accumulatedThirdCnt": 0,
//        "baseDate": "2021-03-11 00:00:00",
//        "firstCnt": 66,
//        "id": 9,
//        "secondCnt": 0,
//        "sido": "세종특별자치시",
//        "thirdCnt": 0,
//        "totalFirstCnt": 860,
//        "totalSecondCnt": 0,
//        "totalThirdCnt": 0
//
//    ],
//    "matchCount": 4578,
//    "page": 1,
//    "perPage": 10,
//    "totalCount": 4578
//}
interface VaccinatedAPI {

    @GET("api/15077756/v1/vaccine-stat")
    fun getVaccinatedData(
        @Query("page") page: Int,
        @Query("perPage") perPage: Int = 10,
        @Query("serviceKey") serviceKey: String = AUTH_KEY
    ): Call<VaccinatedInfo>

    companion object{
        private const val BASE_URL_ROUTE = "https://api.odcloud.kr"
        private const val AUTH_KEY = "l6b43hXTQy1v15AZoeRum1aQ/EU5RDgc7lpqswj6UpPS6+lLk5WkwWo1/LYv+YJd9mM9v8bcdiaCaoV+Uxwrtw=="

        private val client = OkHttpClient.Builder()
            .addInterceptor(Interceptor{
                val request = it.request()
                    .newBuilder()
                    .addHeader("Authorization", AUTH_KEY)
                    .build()
                return@Interceptor it.proceed(request)
            }).build()
        //2021-03-11 00:00:00
        var gson= GsonBuilder().setDateFormat("yyyy-MM-dd' 'HH:mm:ss").create();
        fun create(): VaccinatedAPI{
            return Retrofit.Builder()
                .baseUrl(BASE_URL_ROUTE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(VaccinatedAPI::class.java)
        }
    }
}