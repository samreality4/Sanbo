package com.sxg.sam.sanbo

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.gcm.Task
import com.google.android.gms.gcm.TaskParams
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.Places.getGeoDataClient
import com.google.android.gms.location.places.Places.getPlaceDetectionClient

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Tasks
import com.sxg.sam.sanbo.Model.ResultData
import com.sxg.sam.sanbo.Model.YelpData
import com.sxg.sam.sanbo.Remote.APIService
import com.sxg.sam.sanbo.Remote.ApiClient
import kotlinx.android.synthetic.main.activity_maps.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.Permissions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    //var API_KEY = resources.getString(R.string.apikey);

    val apiKey:String = BuildConfig.YelpApiKey

    var resultlocation: Location = Location("")

    private var UPDATE_INTERVAL: Long = 10 * 1000 //10 secs

    private var FASTEST_INTERVAL: Long = 2000 //2 secs

    var mLocationPermissionGranted: Boolean = false

    private lateinit var geoDataClient: GeoDataClient

    private lateinit var placeDetectionClient: PlaceDetectionClient

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private var mMap: GoogleMap? = null

    private val LOCATION_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        // Construct a GeoDataClient.
        getGeoDataClient(this)

        // Construct a PlaceDetectionClient.
        getPlaceDetectionClient(this)

        // Construct a FusedLocationProviderClient.
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)





        if (checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION")
                == PackageManager.PERMISSION_GRANTED) {
            mMap?.isMyLocationEnabled = true
        } else {
            // Show rationale and request permission.
        }



        fab.setOnClickListener { view ->

            startLocationUpdates()


            /*fusedLocationClient.lastLocation.addOnSuccessListener {  location : Location? ->


                if (location != null) {
                    currentlatitude = location.latitude
                }
                if(location != null){
                    currentLongitude = location.longitude
                }
            }
            val currentPosition = LatLng (currentlatitude, currentLongitude)*/


        }

        var apiService : APIService = ApiClient.getClient().create(APIService::class.java)
        var call : Call<ResultData> = apiService.getYelpData(apiKey)
        call.enqueue(object : Callback<ResultData>{
            override fun onResponse(call: Call<ResultData>?, response: Response<ResultData>?) {
                var yelpList: List<YelpData> = response?.body()?.results!!
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })


    }


    /*fun onLocationChanged(location: Location) {
        // New location has now been determined
        val msg = "Updated Location: " +
                java.lang.Double.toString(location.latitude) + "," +
                java.lang.Double.toString(location.longitude)
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        // You can now create a LatLng Object for use with maps
        */


    protected fun startLocationUpdates() {

        var locationRequest: LocationRequest = LocationRequest.create()

        //Create the location request to start getting updates
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(UPDATE_INTERVAL)
        locationRequest.setFastestInterval(FASTEST_INTERVAL)

        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()

        builder.addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()

        val settingsClient: SettingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)


        if (checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION")
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
            fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    // do work here
                    val latLng = LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
                    mMap?.addMarker(MarkerOptions().position(latLng).title("I am here"))
                    mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                }

            },
                    Looper.myLooper())


        } else {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                    LOCATION_REQUEST_CODE)
            mLocationPermissionGranted = false
            Toast.makeText(this, "Needs Permission", Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap?.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(sydney))

    }

    private fun requestPermission(permissionType: String,
                                  requestCode: Int) {

        ActivityCompat.requestPermissions(this,
                arrayOf(permissionType), requestCode
        )
    }




    }



//find restaurants that are nearby you right now and at your current location.
