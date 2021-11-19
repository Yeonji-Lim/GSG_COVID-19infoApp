package com.example.covid_19info.data


import android.util.Log
import com.example.covid_19info.data.model.LoggedInUser
import com.example.covid_19info.data.model.Quarantine
import com.example.covid_19info.data.model.Quarantines
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST

//로그인 api
interface LoginAPI {

    @POST("/rest-auth")
    fun login(

    ): Call<LoggedInUser>

    @POST("/registration")
    fun registration(

    ): Call<LoggedInUser>


    companion object {
        private const val BASE_URL_ROUTE = "서버 주소"
        fun create(): QuarantinesRouteAPI {
            return Retrofit.Builder()
                .baseUrl(BASE_URL_ROUTE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(QuarantinesRouteAPI::class.java)
        }
    }
}