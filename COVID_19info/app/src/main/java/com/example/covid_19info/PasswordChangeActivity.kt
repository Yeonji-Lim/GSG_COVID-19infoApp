package com.example.covid_19info

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.covid_19info.databinding.ActivityPasswordChangeBinding
import com.example.covid_19info.ui.login.LoginViewModel

class PasswordChangeActivity() : AppCompatActivity(){
    private lateinit var binding : ActivityPasswordChangeBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreference = getSharedPreferences("other",0)
        val editor = sharedPreference.edit()
        
        val title = binding.pwChangeTitle
        val email = binding.pwchangeEmail
        val certificationNum = binding.certificationNum
        val newPW= binding.newPw
        val newPWConfirm = binding.newPwConfirm
        
        //로그인에서 넘어왔는지 info에서 넘어왔는지 판별
        if(intent.getBooleanExtra("is_find",true)){
            title.text = "PW 찾기"
        }
        else{
            title.text = "PW 변경"
        }

        binding.changePwBtn.setOnClickListener {
            if(email.text.toString() == "" || email.text.toString() != sharedPreference.getString("PW","123456"))
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
