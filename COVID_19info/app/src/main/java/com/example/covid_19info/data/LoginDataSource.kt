package com.example.covid_19info.data

import android.util.Log
import com.example.covid_19info.data.model.*
import com.example.covid_19info.ui.login.PWResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    var loginapi = LoginAPI.create()
    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        return withContext(Dispatchers.IO){
            Log.d("main", "실행전")
            try {
                var loginRst = loginapi.login(LoginRequest(username, password)).execute().body()
                //로그인 성공
                Log.d("main", loginRst?.token!!)
                return@withContext Result.Success(LoggedInUser(username, username, loginRst?.token))
            } catch (e: Throwable) {
                return@withContext Result.Error(IOException("Error logging in", e))
            }
        }
    }

    suspend fun logout(tok: String): LoginToken {
        return withContext(Dispatchers.IO){
            try{
                var rst = loginapi.logout("Token "+tok).execute().body()
                Log.d("main", rst.toString())
                return@withContext rst!!
            }catch (e: Throwable){
                Log.d("main", e.toString())
                return@withContext LoginToken("")
            }
        }
    }

    suspend fun signup(email: String, password: String): SignUpRst {
        return withContext(Dispatchers.IO){
            Log.d("main", "실행전")
            try {
                var signUpRst = loginapi.signup(SignUpRst(email, password)).execute().body()
                //회원가입 성공
                //Log.d("main", signUpRst?.email!!)
                return@withContext signUpRst!!
            } catch (e: Throwable) {
                return@withContext SignUpRst("","")
            }
        }
    }

    suspend fun pwChange(pw: String, code: String): ChangeResult<PwchangeUser> {
        return withContext(Dispatchers.IO){
            try {
                var signupEmail = loginapi.pwChange(pw, code).execute().body()
                //이메일 성공
               // Log.d("main", signupEmail?.code!!)
                return@withContext ChangeResult.Success(PwchangeUser(""))
            } catch (e: Throwable) {
                return@withContext ChangeResult.Error(IOException("Error logging in", e))
            }
        }
    }

    suspend fun verifyEmail(email: String): String{
        return withContext(Dispatchers.IO){
            try {
                var email = loginapi.verifyEmail(email).execute().body()
                //이메일 성공
                // Log.d("main", signupEmail?.code!!)
                return@withContext email!!
            } catch (e: Throwable) {
                return@withContext ""
            }
        }
    }
}