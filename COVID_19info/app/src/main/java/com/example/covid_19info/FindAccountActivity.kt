package com.example.covid_19info

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.covid_19info.databinding.ActivityFindAccountBinding
import com.example.covid_19info.ui.register.RegisterViewModel
import com.example.covid_19info.ui.register.RegisterViewModelFactory

class FindAccountActivity(): AppCompatActivity() {


    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding : ActivityFindAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val is_ID = intent.extras?.getBoolean("is_ID")

        val sharedPreference = getSharedPreferences("other",0)
        val editor = sharedPreference.edit()

        val email = binding.findEmail
        val verifiyBtn = binding.findVerifyemail
        val code = binding.findCertificationNum
        val textManual = binding.findAccountManual
        val title = binding.findAccountTitle
        val backspaceBtn = binding.backspaceFingButton
        val findBtn = binding.findBtn
        val emailTitle = binding.emailTitle
        val verifyNumTitle =binding.verifynumTitle

        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory())
            .get(RegisterViewModel::class.java)

        if(is_ID==true){
            title.text = "ID 찾기"
            textManual.text = "본인확인용 이메일 주소와 인증번호를 입력해주세요"
            verifiyBtn.visibility= View.VISIBLE
            verifyNumTitle.text = "인증 번호"
            code.hint="인증 번호를 입력해주세요"

        }
        else{
            title.text = "PW 찾기"
            textManual.text = "비밀번호를 찾고자 하는 계정의 이메일을 입력해주세요"
            verifiyBtn.visibility= View.GONE
            verifyNumTitle.text = "인증 번호"
            code.visibility= View.GONE
        }

        findBtn.setOnClickListener {
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

        backspaceBtn.setOnClickListener {
            super.onBackPressed()
        }
    }
}


