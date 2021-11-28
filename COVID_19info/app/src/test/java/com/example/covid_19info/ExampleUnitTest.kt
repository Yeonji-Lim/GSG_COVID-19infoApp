package com.example.covid_19info


import android.util.Log
import org.junit.Test
import org.junit.Assert.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import com.example.covid_19info.data.LoginAPI
import com.example.covid_19info.data.QuarantinesAPI
import com.example.covid_19info.data.VaccinatedAPI
import com.example.covid_19info.data.model.LoginToken
import com.example.covid_19info.data.model.Quarantines
import com.example.covid_19info.data.model.SignUpRst
import java.text.SimpleDateFormat
import java.time.LocalDate


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        var cdf =  SimpleDateFormat("yyyy-MM-dd")

        var ML = Date()
        var MT = Date()
        var a = LocalDate.now()



    }
}