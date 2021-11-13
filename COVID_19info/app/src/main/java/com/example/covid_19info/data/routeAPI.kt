package com.example.covid_19info.data

import org.json.JSONObject
import java.net.URLDecoder
import java.net.URLEncoder
import java.net.URL
import java.net.HttpURLConnection
import java.io.BufferedReader
import java.io.InputStreamReader

object routeAPI {
    var data = JSONObject()

    init{
        var url = URL("https://coroname.me/getdata")
        var conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Content-type", "application/json")
        var rd:BufferedReader
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

        data = JSONObject(sb.toString().trimIndent())
    }
}