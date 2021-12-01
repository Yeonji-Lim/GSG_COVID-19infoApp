package com.example.covid_19info

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.covid_19info.databinding.ActivityRegisterBinding
import com.example.covid_19info.ui.register.*

class registerActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val email = binding.email
        val password = binding.password
        val passwordConfirm = binding.passwordConfirm
        val register = binding.register
        val loading = binding.loading

        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory())
            .get(RegisterViewModel::class.java)

        registerViewModel.registerFormState.observe(this@registerActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            register.isEnabled = loginState.isDataValid

            if (loginState.emailError != "") {
                email.error = loginState.emailError
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        binding.backspaceRegisterButton.setOnClickListener {
            super.onBackPressed()
        }

        registerViewModel.registerResult.observe(this@registerActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        email.afterTextChanged {
            registerViewModel.registerDataChanged(
                email.text.toString(),
                password.text.toString()
            )
        }



        password.apply {
            afterTextChanged {
                registerViewModel.registerDataChanged(
                    email.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        registerViewModel.signup(
                            email.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }


                /*login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }*/
            register.setOnClickListener{
                if(password.text.toString()!=passwordConfirm.text.toString()){
                    Toast.makeText(this@registerActivity, "새 비밀번호를 다시 확인해 주세요", Toast.LENGTH_SHORT).show()
                }
                else{
                    registerViewModel.signup(email.text.toString(), password.text.toString())

                    val mDialogView = LayoutInflater.from(this@registerActivity).inflate(R.layout.dialog_confirm,null)
                    val mBuilder = AlertDialog.Builder(this@registerActivity)
                        .setView(mDialogView)

                    val mAlertDialog = mBuilder.show()
                    mDialogView.findViewById<TextView>(R.id.dialog_confirm_text).text="인증 메일이 발송되었습니다."

                    //확인버튼
                    val yesBtn = mDialogView.findViewById<Button>(R.id.confirm_btn)
                    yesBtn.text="OK"
                    yesBtn.setOnClickListener{
                        mAlertDialog.dismiss()
                        super.onBackPressed()

                    }
                }
            }
        }

    }

    private fun updateUiWithUser(model: RegisterUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}