// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.example.covid_19info

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.covid_19info.BuildConfig.MAPS_API_KEY
import com.example.covid_19info.data.model.MyLocationDatabase
import com.example.covid_19info.ui.routes.RoutesFragment
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * An activity that displays a map showing the place at the device's current location.
 */
class MainActivity : AppCompatActivity() {


    // [START maps_current_place_on_create]
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //preferenceutil초기화
        PreferenceUtil.context = applicationContext

        setContentView(R.layout.activity_main)

        //로그인 창 이동 구현
        val profileBtn = findViewById<ImageButton>(R.id.user_profile_button)
        profileBtn.setOnClickListener {
            var logindata = getSharedPreferences("login", MODE_PRIVATE)

            val intent:Intent
            //로그인 여부에 따라 창 결정
            if(logindata.getString("token", null)==null){
                intent = Intent(this@MainActivity, LoginActivity::class.java)
            }else{
                intent = Intent(this@MainActivity, UserInfoActivity::class.java)
            }

            startActivity(intent)
        }

        //bottom_navigation listener 설정
        var bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNav.setOnItemSelectedListener { item ->
            changeFragment(
                when(item.itemId) {
                    R.id.page_routes -> {
                        //버튼 상태 변경
                        RoutesFragment()
                    }
                    else -> {
                        RoutesFragment()
                    }
                }
            )
            return@setOnItemSelectedListener true
        }

        //처음 선택된 아이템 설정
        bottomNav.selectedItemId = R.id.page_routes
    }

    //프래그먼트 변경 함수
    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_background, fragment)
            .commit()
    }

    /**
     * Sets up the options menu.
     * @param menu The options menu.
     * @return Boolean.
     */
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.current_place_menu, menu)
//        return true
//    }

}
