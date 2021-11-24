package com.example.covid_19info.ui

import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import android.media.MediaPlayer
import org.json.JSONObject
import android.os.Bundle
import android.widget.TextView
import com.example.covid_19info.R
import org.json.JSONException

class Data_test : AppCompatActivity(){
       private val jsonString = """
        {
        "person": [
                  {
                    "id": 0,
                    "name": "Mathews Parker",
                    "email": "mathewsparker@franscene.com"
                  },
                  {
                    "id": 1,
                    "name": "Dickson Clements",
                    "email": "dicksonclements@franscene.com"
                  },
                  {
                    "id": 2,
                    "name": "Pat Blair",
                    "email": "patblair@franscene.com"
                  },
                  {
                    "id": 3,
                    "name": "Estela Mckinney",
                    "email": "estelamckinney@franscene.com"
                  },
                  {
                    "id": 4,
                    "name": "Rivera Mcclain",
                    "email": "riveramcclain@franscene.com"
                  }
                ]
        }
    """.trimIndent()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.data_testing)

            try{
                val userInfo = JSONObject(jsonString).getJSONObject("person")
                val userName = userInfo.getString("name")
                val userId = userInfo.getString("id")
                val userEmail = userInfo.getString("email")
                var textView2: TextView = findViewById(R.id.textView2)
                textView2.text = "이름: $userName\n id: $userId"+
                        "이메일: $userEmail"
            } catch (e: JSONException){
                e.printStackTrace()
            }

        }
    }