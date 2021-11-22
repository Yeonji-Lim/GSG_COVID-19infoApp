package com.example.covid_19info.data


import android.util.Log
import com.example.covid_19info.data.model.LoginRequest
import com.example.covid_19info.data.model.LoginToken
import com.example.covid_19info.data.model.SignUpRst
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST


interface LoginAPI {

    @Headers("Content-Type: application/json")
    @POST("signup/")
    fun signup(
    @Body signUpRst: SignUpRst
    ): Call<SignUpRst>

    @POST("login/")
    fun login(
        @Body LoginRequest: LoginRequest
    ): Call<LoginToken>

    companion object{
        private const val BASE_URL_ROUTE="https://dcec53b3-9e80-4db3-954c-9605d320a60d.mock.pstmn.io/"
        fun create(): LoginAPI{
            return Retrofit.Builder()
                .baseUrl(BASE_URL_ROUTE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LoginAPI::class.java)
        }

    }
}
