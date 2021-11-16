package com.example.covid_19info

import android.util.Log
import com.example.covid_19info.data.QuarantinesRouteAPI
import org.junit.Test

import org.junit.Assert.*

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        //var i = routeAPI.data.keys();

       // while(i.hasNext())
        {
           // var a = i.next().toString();
            //print(a);
        }

        var url = URL("https://coroname.me/getdata")
        //var url = URL("")
        var conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Content-type", "application/json")
        var rd: BufferedReader
        rd = if(conn.responseCode in 200..300) {
            BufferedReader(InputStreamReader(conn.inputStream))
        } else {
            BufferedReader(InputStreamReader(conn.errorStream))
        }

        var sb = StringBuilder()
        var line = rd.readLine()
        while (line != null) {
            sb.append(line)
            line = rd.readLine()
        }
        rd.close()
        conn.disconnect()

        var data = JSONObject(sb.toString().trimIndent()).getJSONObject("data")


        var temp = data.getString("region")
        print("$temp")


    }
    @Test
    fun addition_isCorrect() {
        //var q = QuarantinesRouteAPI
        var data = QuarantinesRouteAPI.data
        data.data[0]
    }
}