package com.example.covid_19info.data

import android.util.Log
import okhttp3.Call
import org.json.JSONObject
import retrofit2.http.GET
import com.example.covid_19info.data.model.Quarantine

import java.net.URLDecoder
import java.net.URLEncoder
import java.net.URL
import java.net.HttpURLConnection
import java.io.BufferedReader
import java.io.InputStreamReader

//object routeAPI {
//    var data = JSONObject()
//
//    init{
//        var client = DefaultHttpClient()
//
//
//        var url = URL("https://coroname.me/getdata")
//        //var url = URL("")
//        var conn = url.openConnection() as HttpURLConnection
//        conn.requestMethod = "GET"
//        conn.setRequestProperty("Content-type", "application/json")
//        var rd:BufferedReader
//        Log.d("main2", "${conn.responseCode}")
//        rd = if(conn.responseCode in 200..300) {
//            BufferedReader(InputStreamReader(conn.inputStream))
//        } else {
//            BufferedReader(InputStreamReader(conn.errorStream))
//        }
//
//        var sb = StringBuilder()
//        var line = rd.readLine()
//        while (line != null) {
//            sb.append(line)
//            line = rd.readLine()
//        }
//        rd.close()
//        conn.disconnect()
//
//        data = JSONObject(sb.toString().trimIndent())
//    }
//}

interface routeAPI{
//    @GET("getdata")
//    fun getData(
//
//    ): Call<Quarantine>
}