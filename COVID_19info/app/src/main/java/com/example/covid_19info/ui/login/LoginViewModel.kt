package com.example.covid_19info.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.example.covid_19info.data.LoginRepository
import com.example.covid_19info.data.Result
import com.example.covid_19info.data.ChangeResult

import com.example.covid_19info.R
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _pwForm = MutableLiveData<PWFormState>()
    val pwFormState: LiveData<PWFormState> = _pwForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _pwResult = MutableLiveData<PWResult>()
    val pwResult: LiveData<PWResult> = _pwResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        viewModelScope.launch {
            val result = loginRepository.login(username, password)

            if (result is Result.Success) {
                _loginResult.value =
                    LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
            } else {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            loginRepository.logout()
        }
    }

    fun pwChange(pw: String, code: String){
        viewModelScope.launch {
            val result = loginRepository.pwChange(pw, code)

            if(result is ChangeResult.Success){
                _pwResult.value = PWResult(success = R.string.pw_change_success)
            }
            else{
                _pwResult.value = PWResult(error = R.string.pw_change_fail)
            }
        }
    }

    fun verifyEmail(email: String){
        viewModelScope.launch {
           loginRepository.verifyEmail(email)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    fun pwDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _pwForm.value = PWFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _pwForm.value = PWFormState(passwordError = R.string.invalid_password)
        } else {
            _pwForm.value = PWFormState(isDataValid = true)
        }
    }


    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}