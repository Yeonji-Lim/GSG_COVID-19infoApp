package com.example.covid_19info

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.covid_19info.databinding.ActivityPasswordChangeBinding
import android.widget.EditText as EditText1

class PasswordChangeActivity() : AppCompatActivity(){
    private lateinit var binding : ActivityPasswordChangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreference = getSharedPreferences("other",0)
        val editor = sharedPreference.edit()

        val existingPW = binding.existingPw
        val newPW= binding.newPw
        val newPWConfirm = binding.newPwConfirm

        binding.changePwBtn.setOnClickListener {
            if(existingPW.text.toString() == "" || existingPW.text.toString() != sharedPreference.getString("PW","123456"))
                Toast.makeText(this, "기존 비밀번호가 틀립니다", Toast.LENGTH_SHORT).show()
            else if(newPW.text.toString()!=newPWConfirm.text.toString())
                Toast.makeText(this, "새 비밀번호를 다시 확인해 주세요", Toast.LENGTH_SHORT).show()
            else{
                editor.putString("PW",newPW.text.toString())
                editor.apply()
                Toast.makeText(this, "변경 완료", Toast.LENGTH_SHORT).show()
                super.onBackPressed()
            }
        }

        binding.backspacePwchangeButton.setOnClickListener {
            super.onBackPressed()
        }
    }
}
