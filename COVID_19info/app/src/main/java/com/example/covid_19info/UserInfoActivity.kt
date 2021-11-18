package com.example.covid_19info

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.covid_19info.databinding.ActivityUserinfoBinding

class UserInfoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserinfoBinding
    private val prefsFileName = "prefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreference = getSharedPreferences("other",0)
        val editor = sharedPreference.edit()

        //뒤로가기 터치
        binding.backspaceInfoButton.setOnClickListener {
            super.onBackPressed()
        }

        //이메일주소
        binding.emailContent.text =sharedPreference.getString("userEmail","covidinfoapp@gmail.com")

        //비밀번호 변경 터치 시
        binding.passwordChangeBackground.setOnClickListener{
            startActivity(Intent(this,PasswordChangeActivity::class.java))
        }

        //위치정보 제공동의 터치 시
        binding.gpsinfoAgreementBackground.setOnClickListener{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_gps_agreement,null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

            val mAlertDialog = mBuilder.show()

            mDialogView.findViewById<CheckBox>(R.id.gps_agree_chk).isChecked = sharedPreference.getBoolean("gpsAgreement",false)

            //취소버튼
            val noBtn = mDialogView.findViewById<Button>(R.id.no_gps_btn)
            noBtn.setOnClickListener{
                mAlertDialog.dismiss()
            }

            //확인버튼
            val yesBtn = mDialogView.findViewById<Button>(R.id.yes_gps_btn)
            yesBtn.setOnClickListener{
                editor.putBoolean("gpsAgreement", mDialogView.findViewById<CheckBox>(R.id.gps_agree_chk).isChecked)
                editor.apply()
                mAlertDialog.dismiss()
            }
        }

        //로그아웃 터치 시
        binding.logoutBackground.setOnClickListener{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_logout,null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

            val mAlertDialog = mBuilder.show()

            //취소버튼
            val noBtn = mDialogView.findViewById<Button>(R.id.no_logout_btn)
            noBtn.setOnClickListener{
                mAlertDialog.dismiss()
            }

            //확인버튼
            val yesBtn = mDialogView.findViewById<Button>(R.id.yes_logout_btn)
            yesBtn.setOnClickListener{
                //로그아웃 하는 함수 내용 구현 해야함
                //loginViewModel.logout()
                Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
                super.onBackPressed()

            }
        }

    }
}