package com.example.covid_19info


import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.covid_19info.data.LocationUpdateViewModel
import com.example.covid_19info.databinding.ActivityUserinfoBinding
import com.example.covid_19info.ui.login.LoginViewModel
import com.example.covid_19info.ui.login.LoginViewModelFactory
import com.google.android.material.snackbar.Snackbar

@RequiresApi(Build.VERSION_CODES.M)
class UserInfoActivity : AppCompatActivity(), LifecycleOwner {
    private lateinit var binding : ActivityUserinfoBinding
    private val prefsFileName = "prefs"
    private lateinit var loginViewModel: LoginViewModel

    //location view model 초기화
    private val locationUpdateViewModel by lazy {
        ViewModelProvider(this).get(LocationUpdateViewModel::class.java)
    }
    //허용할 권한 설정
    private var permissionRequestType: PermissionRequestType? = null

    // If the user denied a previous permission request, but didn't check "Don't ask again", these
    // Snackbars provided an explanation for why user should approve, i.e., the additional
    // rationale.
    private val fineLocationRationalSnackbar by lazy {
        Snackbar.make(
            binding.userinfo,
            R.string.fine_location_permission_rationale,
            Snackbar.LENGTH_LONG
        )
            .setAction(R.string.ok) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE
                )
            }
    }
    private val backgroundRationalSnackbar by lazy {
        Snackbar.make(
            binding.userinfo,
            R.string.background_location_permission_rationale,
            Snackbar.LENGTH_LONG
        )
            .setAction(R.string.ok) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    REQUEST_BACKGROUND_LOCATION_PERMISSIONS_REQUEST_CODE
                )
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PreferenceUtil.context = applicationContext
        var pref = PreferenceUtil()

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        binding = ActivityUserinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreference = getSharedPreferences("other",0)
        val editor = sharedPreference.edit()

        //요청할 permission 결정
        permissionRequestType = PermissionRequestType.FINE_LOCATION

        //뒤로가기 터치
        binding.backspaceInfoButton.setOnClickListener {
            super.onBackPressed()
        }

        //이메일주소
        binding.emailContent.text = pref.getString("userID", "")

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
            val agreement_contens = mDialogView.findViewById<TextView>(R.id.agreement_contents)
            agreement_contens.movementMethod = ScrollingMovementMethod.getInstance()
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

                //권한 요청
                when (permissionRequestType) {
                    PermissionRequestType.FINE_LOCATION ->
                        requestFineLocationPermission()

                    PermissionRequestType.BACKGROUND_LOCATION ->
                        requestBackgroundLocationPermission()
                }
                mAlertDialog.dismiss()
            }
        }

        binding.withdrawalBackground.setOnClickListener{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_yes_no,null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

            val mAlertDialog = mBuilder.show()
            mDialogView.findViewById<TextView>(R.id.dialog_text).text="정말 탈퇴 하시겠습니까?"
            //취소버튼
            val noBtn = mDialogView.findViewById<Button>(R.id.no_btn)
            noBtn.setOnClickListener{
                mAlertDialog.dismiss()
            }

            //확인버튼
            val yesBtn = mDialogView.findViewById<Button>(R.id.yes_btn)
            yesBtn.text="탈퇴"
            yesBtn.setOnClickListener{
                //로그아웃 하는 함수 내용
                loginViewModel.logout()

                mAlertDialog.dismiss()

                Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
                super.onBackPressed()

            }
        }

        //옵저버 등록
        locationUpdateViewModel.receivingLocationUpdates.observe(
            this,
            androidx.lifecycle.Observer { receivingLocation ->
                Log.d("login", "receiving \n$receivingLocation")
                updateRecordButtonState(receivingLocation)
            }
        )
        //새로운 데이터 갱신 시
        locationUpdateViewModel.locationListLiveData.observe(
            this,
            androidx.lifecycle.Observer { locations ->
                locations?.let {
                    Log.d("login", "Got ${locations.size} locations")
                    val outputStringBuilder = StringBuilder("")
                    for (location in locations) {
                        outputStringBuilder.append(location.toString() + "\n")
                    }
                    Log.d("login", "\n$outputStringBuilder")
                }
            }
        )

        //로그아웃 터치 시
        binding.logoutBackground.setOnClickListener{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_yes_no,null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

            val mAlertDialog = mBuilder.show()
            mDialogView.findViewById<TextView>(R.id.dialog_text).text="정말 로그아웃 하시겠습니까?"
            
            //취소버튼
            val noBtn = mDialogView.findViewById<Button>(R.id.no_btn)
            noBtn.setOnClickListener{
                mAlertDialog.dismiss()
            }

            //확인버튼
            val yesBtn = mDialogView.findViewById<Button>(R.id.yes_btn)
            yesBtn.text="로그아웃"
            yesBtn.setOnClickListener{
                //로그아웃 하는 함수 내용
                loginViewModel.logout()

                mAlertDialog.dismiss()

                Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
                super.onBackPressed()

            }
        }
    }
    //버튼 업데이트
    private fun updateRecordButtonState(receivingLocation: Boolean){
        if (receivingLocation) {
            binding.recordLayout.apply {
                binding.recordText.text = getString(R.string.stop_receiving_location)
                setOnClickListener {
                    locationUpdateViewModel.stopLocationUpdates()
                }
            }
        } else {
            binding.recordLayout.apply {
                binding.recordText.text = getString(R.string.start_receiving_location)
                setOnClickListener {
                    locationUpdateViewModel.startLocationUpdates()
                }
            }
        }
    }
    private fun requestFineLocationPermission() {
        val permissionApproved =
            application?.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) ?: return

        if (permissionApproved) {
            //activityListener?.displayLocationUI()
        } else {
            requestPermissionWithRationale(
                Manifest.permission.ACCESS_FINE_LOCATION,
                REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE,
                fineLocationRationalSnackbar)
        }
    }

    private fun requestBackgroundLocationPermission() {
        val permissionApproved =
            application?.hasPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) ?: return

        if (permissionApproved) {
            //activityListener?.displayLocationUI()
        } else {
            requestPermissionWithRationale(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                REQUEST_BACKGROUND_LOCATION_PERMISSIONS_REQUEST_CODE,
                backgroundRationalSnackbar)
        }
    }

    fun requestPermissionWithRationale(
    permission: String,
    requestCode: Int,
    snackbar: Snackbar
    ) {
        val provideRationale = shouldShowRequestPermissionRationale(permission)

        if (provideRationale) {
            snackbar.show()
        } else {
            requestPermissions(arrayOf(permission), requestCode)
        }
    }

    companion object{
        private const val ARG_PERMISSION_REQUEST_TYPE =
            "com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.PERMISSION_REQUEST_TYPE"

        private const val REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE = 34
        private const val REQUEST_BACKGROUND_LOCATION_PERMISSIONS_REQUEST_CODE = 56
    }
}

enum class PermissionRequestType {
    FINE_LOCATION, BACKGROUND_LOCATION
}