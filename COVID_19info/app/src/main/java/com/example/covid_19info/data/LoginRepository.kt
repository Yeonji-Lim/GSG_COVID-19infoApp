package com.example.covid_19info.data

import android.util.Log
import com.example.covid_19info.PreferenceUtil
import com.example.covid_19info.data.model.LoggedInUser
import com.example.covid_19info.data.model.PwchangeUser
import com.example.covid_19info.data.model.SendEmail
import com.example.covid_19info.data.model.SignUpRst

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {
    var prefs = PreferenceUtil()


    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    suspend fun logout() {
        val result = dataSource.logout(prefs.getString("token",""))

        Log.d("main", "logout end")
        setLoggedOutUser()
//        user = null
    }

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        // handle login
        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedOutUser() {
        prefs.delString("token")
        prefs.delString("userID")
    }

    suspend fun signup(email: String, password: String): SignUpRst {
        return dataSource.signup(email, password)
    }

    suspend fun pwChange(pw: String, code: String): ChangeResult<PwchangeUser> {
        return dataSource.pwChange(pw, code)
    }

    suspend fun verifyEmail(email: String): String{
        return dataSource.verifyEmail(email)
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        prefs.setString("userID", loggedInUser.userId)
        loggedInUser.tok?.let { prefs.setString("token", it) }
        //this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}