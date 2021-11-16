package com.example.covid_19info

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.example.covid_19info.databinding.ActivityPasswordChangeBinding

class PasswordChangeActivity() : AppCompatActivity(){
    private lateinit var binding : ActivityPasswordChangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}