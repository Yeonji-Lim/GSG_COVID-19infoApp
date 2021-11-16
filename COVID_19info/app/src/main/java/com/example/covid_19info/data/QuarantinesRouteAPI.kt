package com.example.covid_19info.data


import android.util.Log
import com.example.covid_19info.data.model.Quarantine
import com.example.covid_19info.data.model.Quarantines
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

//확진자 동선
interface QuarantinesRouteAPI{
    @GET("getdata")
    fun getData(
    ): Call<Quarantines>

    companion object {
        private const val BASE_URL_ROUTE = "https://coroname.me/"
        lateinit var data: Quarantines
        fun create(): QuarantinesRouteAPI {
            return Retrofit.Builder()
                .baseUrl(BASE_URL_ROUTE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(QuarantinesRouteAPI::class.java)
        }
        fun get(): Quarantines {
            val api = QuarantinesRouteAPI.create()
            api.getData().enqueue(object: Callback<Quarantines> {

                override fun onResponse(call: Call<Quarantines>,
                                        response: Response<Quarantines>
                ) {
                    Log.d("Main", "성공 : ${response.raw()}")
                }
                override fun onFailure(call: Call<Quarantines>, t: Throwable) {
                    Log.d("Main", "실패 : ${t.message}")
                }
            })
            return api.getData().execute().body()!!
        }
        init{
            data = get()
        }
    }
}

