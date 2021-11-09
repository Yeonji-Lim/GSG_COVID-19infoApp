package com.example.covid_19info.data

import org.json.JSONObject
import java.net.URLDecoder
import java.net.URLEncoder
import java.net.URL
import java.net.HttpURLConnection
import java.io.BufferedReader
import java.io.InputStreamReader

//예방접종 현황
//https://infuser.odcloud.kr/oas/docs?namespace=15077756/v1

//


object StatisticAPI{
    private const val vaccinateURL = "https://api.odcloud.kr/api/15077756/v1/vaccine-stat"
    private const val vaccinateKEY = "l6b43hXTQy1v15AZoeRum1aQ/EU5RDgc7lpqswj6UpPS6+lLk5WkwWo1/LYv+YJd9mM9v8bcdiaCaoV+Uxwrtw=="
    private const val infectedURL = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19InfStateJson"
    private const val infectedKEY = "l6b43hXTQy1v15AZoeRum1aQ/EU5RDgc7lpqswj6UpPS6+lLk5WkwWo1/LYv+YJd9mM9v8bcdiaCaoV+Uxwrtw=="

    //from "20200310" to "20200315"
    fun get_vaccinated(from: String, to: String): JSONObject {
        var urlBuilder = StringBuilder(vaccinateURL) /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + vaccinateKEY) /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode(vaccinateKEY, "UTF-8")) /*공공데이터포털에서 받은 인증키*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")) /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")) /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("startCreateDt","UTF-8") + "=" + URLEncoder.encode(from, "UTF-8")) /*검색할 생성일 범위의 시작*/
        urlBuilder.append("&" + URLEncoder.encode("endCreateDt","UTF-8") + "=" + URLEncoder.encode(to, "UTF-8")) /*검색할 생성일 범위의 종료*/

        return getfromurl(urlBuilder.toString())
    }
    //from "20200310" to "20200315"
    fun get_infected(from: String, to: String): JSONObject {
        var urlBuilder = StringBuilder(infectedURL) /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + infectedKEY) /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode(infectedKEY, "UTF-8")) /*공공데이터포털에서 받은 인증키*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")) /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")) /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("startCreateDt","UTF-8") + "=" + URLEncoder.encode(from, "UTF-8")) /*검색할 생성일 범위의 시작*/
        urlBuilder.append("&" + URLEncoder.encode("endCreateDt","UTF-8") + "=" + URLEncoder.encode(to, "UTF-8")) /*검색할 생성일 범위의 종료*/

        return getfromurl(urlBuilder.toString())
    }
    private fun getfromurl(urlstr : String): JSONObject {
        var url = URL(urlstr)
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

        val jsonObject = JSONObject(sb.toString().trimIndent())
        return jsonObject
    }
}
