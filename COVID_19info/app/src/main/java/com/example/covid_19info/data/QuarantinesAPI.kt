package com.example.covid_19info.data

import com.example.covid_19info.data.model.QuarantinStat
import com.example.covid_19info.data.model.QuarantinStatSido
import com.example.covid_19info.data.model.VaccinatedInfo
import com.google.gson.GsonBuilder
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface QuarantinesAPI {

    @GET("openapi/service/rest/Covid19/getCovid19InfStateJson")
    fun getQuarantineStat(
        @Query("serviceKey") serviceKey: String = AUTH_KEY,
        @Query("pageNo") page: Int,
        @Query("numOfRows") perPage: Int = 10,
        @Query("startCreateDt") startCreateDt: String = "20200310",
        @Query("endCreateDt") endCreateDt: String = "20200315"
    ): Call<QuarantinStat>

    @GET("openapi/service/rest/Covid19/getCovid19SidoInfStateJson")
    fun getQuarantineStatSido(
        @Query("serviceKey") serviceKey: String = AUTH_KEY1,
        @Query("pageNo") page: Int,
        @Query("numOfRows") perPage: Int = 10,
        @Query("startCreateDt") startCreateDt: String = "20200310",
        @Query("endCreateDt") endCreateDt: String = "20200315"
    ): Call<QuarantinStatSido>

    companion object{
        private const val BASE_URL_ROUTE = "http://openapi.data.go.kr/"
        private const val AUTH_KEY = "l6b43hXTQy1v15AZoeRum1aQ/EU5RDgc7lpqswj6UpPS6+lLk5WkwWo1/LYv+YJd9mM9v8bcdiaCaoV+Uxwrtw=="
        private const val AUTH_KEY1 = "l6b43hXTQy1v15AZoeRum1aQ/EU5RDgc7lpqswj6UpPS6+lLk5WkwWo1/LYv+YJd9mM9v8bcdiaCaoV+Uxwrtw=="

        fun create(): QuarantinesAPI{
            return Retrofit.Builder()
                .baseUrl(BASE_URL_ROUTE)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(TikXmlConverterFactory.create(TikXml.Builder().exceptionOnUnreadXml(false).build()))
                .build()
                .create(QuarantinesAPI::class.java)
        }

    }
}