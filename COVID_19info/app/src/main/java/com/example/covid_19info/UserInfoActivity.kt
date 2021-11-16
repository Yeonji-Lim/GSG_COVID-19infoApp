package com.example.covid_19info

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.covid_19info.databinding.ActivityUserinfoBinding

class UserInfoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserinfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoutButton.setOnClickListener{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.logout_dialog,null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

            mBuilder.show()
        }

        binding.gpsinfoAgreementButton.setOnClickListener{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.gps_agreement_dialog,null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

            mBuilder.show()
        }

    }


}