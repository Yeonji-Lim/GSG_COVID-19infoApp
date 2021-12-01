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
        //"Token "+
        @Header("Authorization") Authorization: String
    ): Call<LoginToken>

    @Headers("Content-Type: application/json")
    @POST("password/reset/")
    fun verifyEmail(
        @Body sendEmail: SendEmail
    ): Call<SendEmail>

    //password reset verify
    @Headers("Content-Type: application/json")
    @POST("password/reset/verified/")
    fun pwChange(
        @Body pwchange: PwChange
    ): Call<PwChangeSuccess>

//    @POST("")

    @GET("member_tracing/")
    fun getuserRoute(
        @Header("Authorization") Authorization: String
    ): Call<List<ServerUserLocation>>

    @POST("member_tracing/")
    fun putuserRoute(
        @Header("Authorization") Authorization: String,
        @Body userLocation: ServerUserLocation
    ): Call<Void>

    @DELETE("user_delete/")
    fun withdrawal(
        @Header("Authorization") Authorization: String
    ): Call<Void>

    companion object{
        private const val BASE_URL_ROUTE="http://192.168.1.172:8000"
        fun create(): LoginAPI{
            return Retrofit.Builder()
                .baseUrl(BASE_URL_ROUTE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LoginAPI::class.java)
        }

    }
}
