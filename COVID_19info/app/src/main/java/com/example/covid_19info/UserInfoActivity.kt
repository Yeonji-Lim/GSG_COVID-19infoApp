package com.example.covid_19info

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.covid_19info.databinding.ActivityUserinfoBinding

class UserInfoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserinfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.passwordChangeBackground.setOnClickListener{
            startActivity(Intent(this,PasswordChangeActivity::class.java))
        }

        binding.gpsinfoAgreementBackground.setOnClickListener{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.gps_agreement_dialog,null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

            mBuilder.show()
        }

        binding.logoutBackground.setOnClickListener{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.logout_dialog,null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

            mBuilder.show()
        }

    }
}