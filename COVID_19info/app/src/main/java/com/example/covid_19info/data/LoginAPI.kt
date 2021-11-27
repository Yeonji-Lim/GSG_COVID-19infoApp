package com.example.covid_19info.data


import com.example.covid_19info.data.model.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface LoginAPI {

    @Headers("Content-Type: application/json")
    @POST("signup/")
    fun signup(
        @Body signUpRst: SignUpRst
    ): Call<SignUpRst>

    @Headers("Content-Type: application/json")
    @POST("login/")
    fun login(
        @Body LoginRequest: LoginRequest
    ): Call<LoginToken>

    @Headers("Content-Type: application/json")
    @GET("logout/")
    fun logout(
        @Header("Authorization") Authorization: String
    ): Call<LoginToken>

    @POST("password/reset/")
    fun verifyEmail(
        @Body email: String
    ): Call<String>

    //password reset verify
    @POST("password/reset/verify/")
    fun pwChange(
        @Body code: String,
        @Body password: String
    ): Call<Boolean>

//    @POST("")

    @Headers("Authorization: Token a9aecd63efdd57d11330f2a343c80f5978970935")
    @GET("member_tracing/")
    fun getuserRoute(
    ): Call<List<ServerUserLocation>>

    companion object{
        private const val BASE_URL_ROUTE="http://172.30.1.42:8000"
        fun create(): LoginAPI{
            return Retrofit.Builder()
                .baseUrl(BASE_URL_ROUTE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LoginAPI::class.java)
        }

    }
}
