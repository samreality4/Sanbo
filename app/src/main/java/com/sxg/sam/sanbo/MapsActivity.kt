package com.sxg.sam.sanbo

import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
import kotlinx.android.synthetic.main.activity_maps.*
import java.security.Permissions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback{

    //var API_KEY = resources.getString(R.string.apikey);

    var resultlocation: Location = Location("")

    private var currentlatitude : Double = 0.0

    private var currentLongitude : Double = 0.0

    var mLocationPermissionGranted : Boolean = false

    private lateinit var geoDataClient: GeoDataClient

    private lateinit var placeDetectionClient: PlaceDetectionClient

    private lateinit var fusedLocationClient: FusedLocationProviderClient



    private lateinit var mMap: GoogleMap

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
            mMap.isMyLocationEnabled = true
        } else {
            // Show rationale and request permission.
        }



        fab.setOnClickListener { view ->

            fusedLocationClient.lastLocation.addOnSuccessListener {  location : Location? ->


                if (location != null) {
                    currentlatitude = location.latitude
                }
                if(location != null){
                    currentLongitude = location.longitude
                }
            }



            val currentPosition = LatLng (currentlatitude, currentLongitude)


            Snackbar.make(view, currentlatitude.toString(), Snackbar.LENGTH_SHORT).show()


            mMap.addMarker(MarkerOptions().position(currentPosition).title("I am here"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition))
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
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

    }

    fun getLocationPermission(){
        if(ContextCompat.checkSelfPermission(applicationContext,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)==
                PackageManager.PERMISSION_GRANTED){
            mLocationPermissionGranted = true;
        } else {
        }
    }

}



//find restaurants that are nearby you right now and at your current location.
