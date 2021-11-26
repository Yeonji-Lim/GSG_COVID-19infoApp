package com.example.covid_19info.data

import android.util.Log
import com.example.covid_19info.data.model.LoggedInUser
import com.example.covid_19info.data.model.LoginRequest
import com.example.covid_19info.data.model.LoginToken
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
        return withContext(Dispatchers.Main){
            // TODO: revoke authentication
            try{
                var rst = loginapi.logout(LoginToken(tok)).execute().body()

                return@withContext rst!!
            }catch (e: Throwable){

                return@withContext LoginToken("")
            }
        }
    }
}