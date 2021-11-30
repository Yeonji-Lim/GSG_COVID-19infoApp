package com.example.covid_19info

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.covid_19info.databinding.ActivityPasswordChangeBinding
import com.example.covid_19info.ui.login.LoginViewModel
import com.example.covid_19info.ui.login.LoginViewModelFactory

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
        val change_btn = binding.changePwBtn
        val verify_btn = binding.verifyEmail
        
        //로그인에서 넘어왔는지 info에서 넘어왔는지 판별
        if(intent.getBooleanExtra("is_find",true)){
            title.text = "PW 찾기"
        }
        else{
            title.text = "PW 변경"
        }

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.pwFormState.observe(this, Observer {
            val pwState = it ?: return@Observer

            change_btn.isEnabled = pwState.isDataValid

            if(pwState.usernameError != null){
                email.error = getString(pwState.usernameError)
            }
            if(pwState.passwordError != null){
                newPW.error = getString(pwState.passwordError)
            }
        })

        loginViewModel.pwResult.observe(this, Observer {
            val changeResult = it ?: return@Observer

            if(changeResult.error != null){
                Toast.makeText(applicationContext, changeResult.error, Toast.LENGTH_SHORT).show()
            }
            if(changeResult.success != null){
                Toast.makeText(applicationContext, "비밀번호가 변경되었습니다", Toast.LENGTH_SHORT).show()
            }
            setResult(Activity.RESULT_OK)
            finish()
        })



        email.apply{
            email.afterTextChanged {
                loginViewModel.pwDataChanged(
                    email.text.toString(),
                    newPW.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.pwDataChanged(
                            email.text.toString(),
                            newPW.text.toString()
                        )
                }
                false
            }

            verify_btn.setOnClickListener {
                loginViewModel.verifyEmail(email.text.toString())
                Toast.makeText(this@PasswordChangeActivity, "이메일로 인증 번호를 보냈습니다", Toast.LENGTH_SHORT).show()
            }


        }

        newPW.apply {
            afterTextChanged {
                loginViewModel.pwDataChanged(
                    email.text.toString(),
                    newPW.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.pwDataChanged(
                            email.text.toString(),
                            newPW.text.toString()
                        )
                }
                false
            }

            change_btn.setOnClickListener {
                if(newPW.text.toString()!=newPWConfirm.text.toString())
                    Toast.makeText(this@PasswordChangeActivity, "새 비밀번호를 다시 확인해 주세요", Toast.LENGTH_SHORT).show()
                else{
                    loginViewModel.pwChange(newPW.text.toString(), certificationNum.text.toString())
                    Toast.makeText(this@PasswordChangeActivity, "변경 완료", Toast.LENGTH_SHORT).show()
                    loginViewModel.logout()
                    super.onBackPressed()
                }
            }
        }


        binding.backspacePwchangeButton.setOnClickListener {
            super.onBackPressed()
        }
    }
}
