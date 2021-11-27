package com.example.covid_19info.data

import android.util.Log
import com.example.covid_19info.PreferenceUtil
import com.example.covid_19info.data.model.*
import com.example.covid_19info.ui.login.PWResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    var loginapi = LoginAPI.create()
    var db = PreferenceUtil.context?.let { MyLocationDatabase.getInstance(it) }!!.locationDao()

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        return withContext(Dispatchers.IO){
            Log.d("main", "실행전")
            try {
                var loginRst = loginapi.login(LoginRequest(username, password)).execute().body()
                //로그인 성공
                Log.d("main", loginRst?.token!!)
//                2021-11-27T13:15:32+09:00
                var transform = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
                
                //서버 데이터 가져오기
                var locs = loginapi.getuserRoute("Token "+loginRst?.token!!).execute()
                for(location in locs.body()!!){
                    db.addLocation(MyLocationEntity(UUID.fromString(location.id),
                        location.latitude, location.longtitude, true,
                        transform.parse(location.date)!!
                    ))
                }

                return@withContext Result.Success(LoggedInUser(username, username, loginRst?.token))
            } catch (e: Throwable) {
                Log.d("main", e.toString())
                return@withContext Result.Error(IOException("Error logging in", e))
            }
        }
    }

    suspend fun logout(tok: String): LoginToken {
        return withContext(Dispatchers.IO){
            try{
                //내부 db 삭제
                db.deleteAllLoc()

                //서버로 로그아웃 보냄
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
                Log.d("main", signUpRst.toString())
                return@withContext signUpRst!!
            } catch (e: Throwable) {
                Log.d("main", e.toString())
                return@withContext SignUpRst("","")
            }
        }
    }

    suspend fun pwChange(pw: String, code: String): ChangeResult<PwchangeUser> {
        return withContext(Dispatchers.IO){
            Log.d("main", "변경전")
            try {
                var signupEmail = loginapi.pwChange(PwChange(password = pw, code = code)).execute()
                Log.d("main", signupEmail.toString())
                //이메일 성공
               // Log.d("main", signupEmail?.code!!)
                return@withContext ChangeResult.Success(PwchangeUser(""))
            } catch (e: Throwable) {
                Log.d("main", e.toString())
                return@withContext ChangeResult.Error(IOException("Error logging in", e))
            }
        }
    }

    suspend fun verifyEmail(email: String): String{
        return withContext(Dispatchers.IO){
            try {
                var rst = loginapi.verifyEmail(SendEmail(email)).execute()
                Log.d("main", rst.message())
                Log.d("main", rst.toString())
                var email = rst.body()?.email
                //이메일 성공
                // Log.d("main", signupEmail?.code!!)
                return@withContext email!!
            } catch (e: Throwable) {
                return@withContext ""
            }
        }
    }
}