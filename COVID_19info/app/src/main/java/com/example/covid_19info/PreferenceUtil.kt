package com.example.covid_19info

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("login", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }
    fun delString(key: String){
        prefs.edit().remove(key).apply()
    }
    companion object{
        lateinit var context: Context
    }
}