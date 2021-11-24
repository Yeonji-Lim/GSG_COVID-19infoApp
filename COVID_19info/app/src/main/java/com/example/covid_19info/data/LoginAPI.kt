package com.example.covid_19info.data


import com.example.covid_19info.data.model.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
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

    @POST("logout/")
    fun logout(
        @Body LoginToken: LoginToken
    ): Call<LoginToken>

    @POST("verifyEmail/")
    fun verifyEmail(
        @Body signupEmail: SignupEmail
    ): Call<SignupCode>

    companion object{
        private const val BASE_URL_ROUTE="https://ab4fd298-6573-4ed6-a0f3-987108b4679a.mock.pstmn.io"
        fun create(): LoginAPI{
            return Retrofit.Builder()
                .baseUrl(BASE_URL_ROUTE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LoginAPI::class.java)
        }

    }
}
