package com.example.covid_19info.ui.routes

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import android.view.ViewGroup
import androidx.core.view.*
import kotlin.math.roundToInt
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Build

import android.widget.LinearLayout

import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.findFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.covid_19info.*
import com.example.covid_19info.data.LocationRepository
import com.example.covid_19info.data.QuarantinesRouteAPI
import com.example.covid_19info.data.model.MyLocationDao
import com.example.covid_19info.data.model.MyLocationDatabase
import com.example.covid_19info.data.model.MyLocationEntity
import com.example.covid_19info.data.model.Quarantines
import com.example.covid_19info.databinding.ActivityMainBinding
import com.google.android.gms.maps.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.concurrent.Executors


class RoutesFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding

    // 1. Context를 할당할 변수를 프로퍼티로 선언(어디서든 사용할 수 있게)
    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // 2. Context를 액티비티로 형변환해서 할당
        mainActivity = context as MainActivity
    }

    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null

    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private val defaultLocation = LatLng(37.5642135, 127.0016985)
    private var locationPermissionGranted = false

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null
    private var likelyPlaceNames: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAddresses: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAttributions: Array<List<*>?> = arrayOfNulls(0)
    private var likelyPlaceLatLngs: Array<LatLng?> = arrayOfNulls(0)

    //확진자 동선 데이터
    lateinit private var quarantinesData: Quarantines

    private var quartineMarkerList: MutableList<Marker> = mutableListOf()
    private var userMarkerList: MutableList<Marker> = mutableListOf()
    lateinit var buttons :LinearLayout



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Retrieve the content view that renders the map.
        return inflater.inflate(R.layout.content_route, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // [START_EXCLUDE silent]
        // Retrieve location and camera position from saved instance state.
        // [START maps_current_place_on_create_save_instance_state]
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }
        // [END maps_current_place_on_create_save_instance_state]
        // [END_EXCLUDE]

        // [START_EXCLUDE silent]
        // Construct a PlacesClient
        Places.initialize(mainActivity, BuildConfig.MAPS_API_KEY)
        placesClient = Places.createClient(mainActivity)

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mainActivity)

        // Build the map.
        // [START maps_current_place_map_fragment]
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)


        // [END maps_current_place_map_fragment]
        // [END_EXCLUDE]

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                //Log.i(TAG, "Place: ${place.name}, ${place.id}, ${place.latLng}")
                
                //검색 지역으로 이동
                map?.moveCamera(CameraUpdateFactory
                        .newLatLngZoom(place.latLng, 15f))
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: $status")
            }
        })
        //검색창 색 변경
        autocompleteFragment.view?.setBackgroundColor(Color.WHITE)

        //검색 가능 지역 한국으로 고정
        autocompleteFragment.setCountry("KR")

        //장소 가져오기
//        context?.let { it1 ->
//            LocationRepository.getInstance(it1, Executors.newSingleThreadExecutor())
//                .getLocations()
//        }?.observe(this, { locations ->
//            for(mark in userMarkerList){
//
//                mark.remove()
//            }
//            userMarkerList.clear()
//            //마크생성
//            for (location in locations) {
//                var mark = map?.addMarker(
//                    MarkerOptions()
//                        .title(location.date.toString())
//                        .position((LatLng(location.latitude, location.longitude)))
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
//                )
//                mark?.isVisible = false
//                mark?.let { userMarkerList.add(it) }
//
//            }
//            Log.d("main", "in observe $userMarkerList")
//            return@observe
//        })
        var db = context?.let { MyLocationDatabase.getInstance(it) }!!.locationDao()
        db.getLocations().observe(viewLifecycleOwner, { locations ->

            createUserMarker(locations)
            db.getLocations().removeObservers(viewLifecycleOwner)
        })


        //프래그먼트 내부 버튼 리스너 설정
        //changeView 리스너
        val changeView = view.findViewById<Button>(R.id.changeView)
        changeView.setOnClickListener {
            Log.d("loginButton","Click")
            Log.d("loginButton",changeView.isSelected.toString())
            changeView?.isSelected = changeView?.isSelected != true

            val pref = PreferenceUtil()
            //로그인 안된경우
            if(pref.getString("token", "")==""){
                Log.d("loginButton","OFF")
                val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_yes_no,null)
                val mBuilder = context?.let { it1 ->
                    AlertDialog.Builder(it1)
                        .setView(mDialogView)
                }

                val mAlertDialog = mBuilder?.show()
                mDialogView.findViewById<TextView>(R.id.dialog_text).text="로그인이 필요한 메뉴입니다."

                //취소버튼
                val noBtn = mDialogView.findViewById<Button>(R.id.no_logout_btn)
                noBtn.setOnClickListener{
                    mAlertDialog?.dismiss()
                }

                //확인버튼
                val yesBtn = mDialogView.findViewById<Button>(R.id.yes_logout_btn)
                yesBtn.text="로그인"
                yesBtn.setOnClickListener{
                    //로그인 액티비티로 이동
                    var intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)

                    mAlertDialog?.dismiss()
                }
            }
            //로그인 된 경우
            else {
                for(usermark in userMarkerList){
                    usermark.setVisible(true);

                }
                Log.d("main", "in button listner $userMarkerList")
                mapFragment?.view?.requestLayout()

                Log.d("loginButton","user markers : $userMarkerList")
                Log.d("main",userMarkerList.size.toString())

            }
            //사용자 동선 선택 x
//            if(changeView.isSelected)
//            {
//                Log.d("loginButton","USERTRUE")
//                userMarker(true)
//            }
//            else
//            {
//                Log.d("loginButton","USERFALSE")
//                userMarker(false)
//            }

        }

        //하단 버튼 리스너 설정을 위한 변수 설정
        buttons = view.findViewById<LinearLayout>(R.id.linearLayout)
        //버튼 움직임 설정
        setButtonsMove()

        //네트워크 호출
        val api = QuarantinesRouteAPI.create()
        api.getData().enqueue(object: Callback<Quarantines> {

            override fun onResponse(call: Call<Quarantines>,
                                    response: Response<Quarantines>
            ) {
                response.body()?.let {quarantinesData = it}
                response.body()?.let { showQuarantineRoute(it) }
                Log.d("Main", "성공 : ${response.raw()}")
            }
            override fun onFailure(call: Call<Quarantines>, t: Throwable) {
                Log.d("Main", "실패 : ${t}")
            }
        })

        //showUserRoute()

    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    // [START maps_current_place_on_save_instance_state]
    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(RoutesFragment.KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(RoutesFragment.KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }
    // [END maps_current_place_on_save_instance_state]

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    // [START maps_current_place_on_map_ready]
    override fun onMapReady(map: GoogleMap) {
        this.map = map

        // [START_EXCLUDE]
        // [START map_current_place_set_info_window_adapter]
        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        this.map?.setInfoWindowAdapter(object : InfoWindowAdapter {
            // Return null here, so that getInfoContents() is called next.
            override fun getInfoWindow(arg0: Marker): View? {
                return null
            }

            override fun getInfoContents(marker: Marker): View {
                // Inflate the layouts for the info window, title and snippet.
                val infoWindow = layoutInflater.inflate(
                    R.layout.custom_info_contents,
                    view?.findViewById<FrameLayout>(R.id.map), false)
                val title = infoWindow.findViewById<TextView>(R.id.title)
                title.text = marker.title
                val snippet = infoWindow.findViewById<TextView>(R.id.snippet)
                snippet.text = marker.snippet
                return infoWindow
            }
        })
        // [END map_current_place_set_info_window_adapter]

        // Prompt the user for permission.
        getLocationPermission()
        // [END_EXCLUDE]

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()
    }
    // [END maps_current_place_on_map_ready]

    /**
     * Handles a click on the menu option to get a place.
     * @param item The menu item to handle.
     * @return Boolean.
     */
    // [START maps_current_place_on_options_item_selected]
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.option_get_place) {
//            showCurrentPlace()
//        }
//        return true
//    }
    // [END maps_current_place_on_options_item_selected]

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    // [START maps_current_place_get_device_location]
    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(mainActivity) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map?.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }

            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
    // [END maps_current_place_get_device_location]

    /**
     * Prompts the user for permission to use the device location.
     */
    // [START maps_current_place_location_permission]
    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(mainActivity.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(mainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }
    // [END maps_current_place_location_permission]

    /**
     * Handles the result of the request for location permissions.
     */
    // [START maps_current_place_on_request_permissions_result]
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
        }
        updateLocationUI()
    }
    // [END maps_current_place_on_request_permissions_result]

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    // [START maps_current_place_show_current_place]
    // @SuppressLint("MissingPermission")
    // private fun showCurrentPlace() {
    //     if (map == null) {
    //         return
    //     }
    //     if (locationPermissionGranted) {
    //         // Use fields to define the data types to return.
    //         val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

    //         // Use the builder to create a FindCurrentPlaceRequest.
    //         val request = FindCurrentPlaceRequest.newInstance(placeFields)

    //         // Get the likely places - that is, the businesses and other points of interest that
    //         // are the best match for the device's current location.
    //         val placeResult = placesClient.findCurrentPlace(request)
    //         placeResult.addOnCompleteListener { task ->
    //             if (task.isSuccessful && task.result != null) {
    //                 val likelyPlaces = task.result

    //                 // Set the count, handling cases where less than 5 entries are returned.
    //                 val count = if (likelyPlaces != null && likelyPlaces.placeLikelihoods.size < M_MAX_ENTRIES) {
    //                     likelyPlaces.placeLikelihoods.size
    //                 } else {
    //                     M_MAX_ENTRIES
    //                 }
    //                 var i = 0
    //                 likelyPlaceNames = arrayOfNulls(count)
    //                 likelyPlaceAddresses = arrayOfNulls(count)
    //                 likelyPlaceAttributions = arrayOfNulls<List<*>?>(count)
    //                 likelyPlaceLatLngs = arrayOfNulls(count)
    //                 for (placeLikelihood in likelyPlaces?.placeLikelihoods ?: emptyList()) {
    //                     // Build a list of likely places to show the user.
    //                     likelyPlaceNames[i] = placeLikelihood.place.name
    //                     likelyPlaceAddresses[i] = placeLikelihood.place.address
    //                     likelyPlaceAttributions[i] = placeLikelihood.place.attributions
    //                     likelyPlaceLatLngs[i] = placeLikelihood.place.latLng
    //                     i++
    //                     if (i > count - 1) {
    //                         break
    //                     }
    //                 }

    //                 // Show a dialog offering the user the list of likely places, and add a
    //                 // marker at the selected place.
    //                 openPlacesDialog()
    //             } else {
    //                 Log.e(TAG, "Exception: %s", task.exception)
    //             }
    //         }
    //     } else {
    //         // The user has not granted permission.
    //         Log.i(TAG, "The user did not grant location permission.")

    //         // Add a default marker, because the user hasn't selected a place.
    //         map?.addMarker(MarkerOptions()
    //             .title("123")
    //             .position(defaultLocation)
    //             .snippet("132"))

    //         // Prompt the user for permission.
    //         getLocationPermission()
    //     }
    // }
    // [END maps_current_place_show_current_place]

    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    // [START maps_current_place_open_places_dialog]
    private fun openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        val listener = DialogInterface.OnClickListener { dialog, which -> // The "which" argument contains the position of the selected item.
            val markerLatLng = likelyPlaceLatLngs[which]
            var markerSnippet = likelyPlaceAddresses[which]
            if (likelyPlaceAttributions[which] != null) {
                markerSnippet = """
                    $markerSnippet
                    ${likelyPlaceAttributions[which]}
                    """.trimIndent()
            }

            if (markerLatLng == null) {
                return@OnClickListener
            }

            // Add a marker for the selected place, with an info window
            // showing information about that place.
            map?.addMarker(MarkerOptions()
                .title(likelyPlaceNames[which])
                .position(markerLatLng)
                .snippet(markerSnippet))

            // Position the map's camera at the location of the marker.
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                DEFAULT_ZOOM.toFloat()))
        }

        // Display the dialog.
        AlertDialog.Builder(mainActivity)
            .setTitle("123")
            .setItems(likelyPlaceNames, listener)
            .show()
    }
    // [END maps_current_place_open_places_dialog]

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    // [START maps_current_place_update_location_ui]
    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                //현재위치 버튼 커스텀하기 위해
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = false
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
    // [END maps_current_place_update_location_ui]

    //확진자 마커 등록
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showQuarantineRoute(routes: Quarantines){
        val today = LocalDate.now()
        for(route in routes.data){
            var visitdate = LocalDate.parse(route.visitedDate.substring(0,10), DateTimeFormatter.ISO_DATE)
            val diff = ChronoUnit.DAYS.between(visitdate, today)
            var pos = route.latlng.split(", ").toTypedArray()
            var mark = map?.addMarker(MarkerOptions()
                .title(route.place)
                .position(LatLng(pos[0].toDouble(), pos[1].toDouble()))
                .snippet("${route.visitedDate}\n${route.address}"))
            if(diff in 4..5)
                mark?.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            else if(diff>5)
                mark?.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            mark?.let { quartineMarkerList.add(it) }
        }
    }

    fun createUserMarker(locations : List<MyLocationEntity>){
        Log.d("main", userMarkerList.size.toString())
        if(userMarkerList.size == 0) {
            //마크생성
            for (location in locations) {
                var mark = map?.addMarker(
                    MarkerOptions()
                        .title(location.date.toString())
                        .position((LatLng(location.latitude, location.longitude)))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

                )
                mark?.isVisible = true
                mark?.let { userMarkerList.add(it) }

            }
            Log.d("main", "in observe $userMarkerList")
            Log.d("main", userMarkerList.size.toString())
        }


    }

    //사용자 동선 데이터베이스 테스트
    private fun showUserRoute(){
        var db = context?.let { MyLocationDatabase.getInstance(it)}
        var loc = db?.locationDao()?.getLocations()

        Log.d("tagtag",loc?.value?.get(0).toString())
        var mark = map?.addMarker(MarkerOptions()
            .position((LatLng(loc?.value?.get(0)?.latitude!!,loc?.value?.get(0)?.longitude!!)))
        )
        mark?.let { userMarkerList.add(it) }
        userMarkerList[0].isVisible = true
    }

    fun userMarkervisible(visible : Boolean){
        if(visible)
        {
            for(mark in userMarkerList) {
                mark.isVisible = true
            }
        }
        else
        {
            for(mark in userMarkerList) {
                mark.isVisible = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    fun setButtonsMove(): Boolean {
        //기본 위치
        var gestureListener = MyGesture(buttons)
        var gestureDetector = GestureDetector(context, gestureListener)


        for(item in buttons){
            var btn = item as Button
            //버튼 움직임 리스너 등록
            if(btn.id==R.id.moveButtons){
                btn.setOnTouchListener { view, motionEvent ->
                    Log.d("main", "${motionEvent.actionMasked}")
                    when(motionEvent.actionMasked){
                        MotionEvent.ACTION_UP->{
                            true
                        }
                    }
                    return@setOnTouchListener gestureDetector.onTouchEvent(motionEvent)
                }
            }else{
                //버튼 동작 등록
                btn.setOnClickListener {
                    if(btn.isSelected){
                        btn.isSelected = false
                        markCheck(true)
                    }
                    else{
                        for(i in 1..4){
                            buttons[i].isSelected = false
                        }
                        btn.isSelected = true
                        markCheck(false)
                    }

                    //btn?.isSelected = btn?.isSelected != true
                }
            }
        }

        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    //allvisible이 true이면 모두 표시
    fun markCheck(allvisible: Boolean){
        //모두 표시
        if(allvisible){
            for(mark in quartineMarkerList){
                mark.isVisible = true
            }
            return
        }
        //날짜 차이 이하
        var constraintl = arrayOf(3,5,7,14)
        var constraint = 0
        for(i in 1..4){
            if(buttons[i].isSelected){
                constraint = constraintl[i-1]
                break
            }
        }
        //날짜 계산후 이하만 출력
        val today = LocalDate.now()
        for(mark in quartineMarkerList){
            val visitdate = LocalDate.parse(mark.snippet?.substring(0,10), DateTimeFormatter.ISO_DATE);
            val diff = ChronoUnit.DAYS.between(visitdate, today)
            //Log.d("Main", diff.toString())
            mark.isVisible = diff <= constraint
        }
    }



    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        // [START maps_current_place_state_keys]
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        // [END maps_current_place_state_keys]

        // Used for selecting the current place.
        private const val M_MAX_ENTRIES = 5
    }
}

class MyGesture(val layout: LinearLayout) : GestureDetector.OnGestureListener {
    var startButtonPos = 0
    var btn = (layout[0] as Button)
    val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()
    val THRES_HOLD = 150.px
    // 제스처 이벤트를 받아서 text를 변경
    override fun onShowPress(e: MotionEvent?) {
        Log.d("test1","onShowPress")
        Log.d("test1",e.toString())
    }
    override fun onSingleTapUp(e: MotionEvent?): Boolean {

        btn.isSelected = btn.isSelected != true
        btn.text = if(btn.isSelected) ">" else "<"

        moveButtons(btn.isSelected)
        Log.d("test1","onSingleTapUp")
        Log.d("test1",e.toString())
        return true
    }
    override fun onDown(e: MotionEvent?): Boolean {
        //현재 레이아웃 위치 받아오기
        val params = (layout.layoutParams as ViewGroup.MarginLayoutParams)
        startButtonPos = params.rightMargin

        Log.d("test1","onDown")
        Log.d("test1",e.toString())
        return true
    }
    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        //나오는 방향
        moveButtons(velocityX<0)
        if(velocityX<-20){
            btn.isSelected = true
            btn.text = ">"
        }else{
            btn.isSelected = false
            btn.text = "<"
        }
        Log.d("test1","onFling")
        Log.d("test1",velocityX.toString())
        return true
    }
    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        //마진 데이터
        val params = (layout.layoutParams as ViewGroup.MarginLayoutParams)
        val moved = startButtonPos - e2?.rawX!! + e1?.rawX!!
        //마진 설정
        Log.d("main", "$moved")
        if(moved>-300.px && moved < 0){
            params.updateMargins(right= moved.roundToInt())
        }
        //레이아웃 업데이트
        layout.requestLayout()
        return true
    }
    override fun onLongPress(e: MotionEvent?) {
        Log.d("test1","onLongPress")
        Log.d("test1",e.toString())
    }
    //flg true: 나오는 애니메이션, false: 들어가는 애니메이션
    fun moveButtons(flg: Boolean){
        //val params = (layout.layoutParams as ViewGroup.MarginLayoutParams)

        val params = (layout.layoutParams as ViewGroup.MarginLayoutParams)
        val animator0 = ValueAnimator.ofInt(params.rightMargin, 0)
        animator0.addUpdateListener { valueAnimator ->
            params.rightMargin = (valueAnimator.animatedValue as Int)!!
            layout.requestLayout()
        }
        animator0.duration = 500

        val animator1 = ValueAnimator.ofInt(params.rightMargin, (-300).px)
        animator1.addUpdateListener { valueAnimator ->
            params.rightMargin = (valueAnimator.animatedValue as Int)!!
            layout.requestLayout()
        }
        animator1.duration = 500

        if(flg){
            animator0.start()
        }
        //들어가는 방향
        else{
            animator1.start()
        }
    }
}

