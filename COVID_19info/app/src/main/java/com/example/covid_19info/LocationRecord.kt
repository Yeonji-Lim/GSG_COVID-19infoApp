package com.example.covid_19info

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.app.Activity

import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager

import androidx.core.content.ContextCompat


class LocationService : Service() {
    private var mLocationManager: LocationManager? = null

    var mLocationListeners = arrayOf(LocationListener(LocationManager.GPS_PROVIDER), LocationListener(LocationManager.NETWORK_PROVIDER))
    val inPermission = false
    val locationPermission1 = PackageManager.PERMISSION_DENIED
    val locationPermission2 = PackageManager.PERMISSION_DENIED
    val locationPermission3 = PackageManager.PERMISSION_DENIED


    class LocationListener(provider: String) : android.location.LocationListener {
        internal var mLastLocation: Location

        init {
            Log.e(TAG, "LocationListener $provider")
            mLastLocation = Location(provider)
        }

        override fun onLocationChanged(location: Location) {
            Log.e(TAG, "onLocationChanged: $location")
            mLastLocation.set(location)
            Log.v("LastLocation", mLastLocation.latitude.toString() +"  " + mLastLocation.longitude.toString())
        }

        override fun onProviderDisabled(provider: String) {
            Log.e(TAG, "onProviderDisabled: $provider")
        }

        override fun onProviderEnabled(provider: String) {
            Log.e(TAG, "onProviderEnabled: $provider")
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            Log.e(TAG, "onStatusChanged: $provider")
        }
    }

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand")
        super.onStartCommand(intent, flags, startId)
        return Service.START_STICKY
    }

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        Log.e(TAG, "onCreate")
        //권한 설정
        if(!checkAndRequestPermissions()){
            Log.e("LocRec", "error permission")
            return
        }

        initializeLocationManager()
        try {
            mLocationManager!!.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL.toLong(), LOCATION_DISTANCE,
                mLocationListeners[1])
        } catch (ex: java.lang.SecurityException) {
            Log.i(TAG, "fail to request location update, ignore", ex)
        } catch (ex: IllegalArgumentException) {
            Log.d(TAG, "network provider does not exist, " + ex.message)
        }

        try {
            mLocationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, LOCATION_INTERVAL.toLong(), LOCATION_DISTANCE,
                mLocationListeners[0])
        } catch (ex: java.lang.SecurityException) {
            Log.i(TAG, "fail to request location update, ignore", ex)
        } catch (ex: IllegalArgumentException) {
            Log.d(TAG, "gps provider does not exist " + ex.message)
        }

    }

    @SuppressLint("MissingPermission")
    override fun onDestroy() {
        Log.e(TAG, "onDestroy")
        if(!checkAndRequestPermissions()){
            Log.e("LocRec", "error permission")
            return
        }
        super.onDestroy()
        if (mLocationManager != null) {
            for (i in mLocationListeners.indices) {
                try {
                    mLocationManager!!.removeUpdates(mLocationListeners[i])
                } catch (ex: Exception) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex)
                }

            }
        }
    }

    private fun initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager")
        if (mLocationManager == null) {
            mLocationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
    }

    fun checkAndRequestPermissions(): Boolean {
        val inPermission = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.INTERNET
        )
        val locationPermission1 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val locationPermission2 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val locationPermission3 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )

        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (inPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET)
        }
        if (locationPermission1 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (locationPermission2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (locationPermission3 != PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                (applicationContext as Activity?)!!,
                listPermissionsNeeded.toTypedArray(),
                1
            )
            return false
        }
        return true
    }

    companion object {
        private val TAG = "BOOMBOOMTESTGPS"
        private val LOCATION_INTERVAL = 1000
        private val LOCATION_DISTANCE = 0f
    }
}














//object LocationRecord {
//    private val mFusedLocationClient: FusedLocationProviderClient
//        get() {
//            TODO()
//        }
//    private val locationCallback: LocationCallback = TODO()
//    private val locationRequest: LocationRequest
//    val locationSettingsRequest: LocationSettingsRequest
//
//    private var workable: Workable<GPSPoint>? = null
//    fun onChange(workable: Workable<GPSPoint?>) {
//        this.workable = workable
//    }
//    fun stop() {
//        Log.i(TAG, "stop() Stopping location tracking")
//        mFusedLocationClient.removeLocationUpdates(locationCallback)
//    }
//}
//
//
///**
// * Uses Google Play API for obtaining device locations
// * Created by alejandro.tkachuk
// * alejandro@calculistik.com
// * www.calculistik.com Mobile Development
// */
//class LocationRecord private constructor() {
//
//
//
//
//
//    companion object {
//        private val instance = Wherebouts()
//        private val TAG = Wherebouts::class.java.simpleName
//        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 1000
//        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 1000
//        fun instance(): Wherebouts {
//            return instance
//        }
//    }
//
//    init {
//        locationRequest = LocationRequest()
//        locationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
//        locationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
//        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        val builder = LocationSettingsRequest.Builder()
//        builder.addLocationRequest(locationRequest)
//        locationSettingsRequest = builder.build()
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                super.onLocationResult(locationResult) // why? this. is. retarded. Android.
//                val currentLocation = locationResult.lastLocation
//                val gpsPoint = GPSPoint(currentLocation.latitude, currentLocation.longitude)
//                Log.i(TAG, "Location Callback results: $gpsPoint")
//                if (null != workable) workable.work(gpsPoint)
//            }
//        }
//        mFusedLocationClient =
//            LocationServices.getFusedLocationProviderClient(MainApplication.getAppContext())
//        mFusedLocationClient.requestLocationUpdates(
//            locationRequest,
//            locationCallback, Looper.myLooper()
//        )
//    }
//}