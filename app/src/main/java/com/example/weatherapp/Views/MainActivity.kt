package com.example.weatherapp.Views

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.perpuletask.MainActivityViewModel
import com.example.weatherapp.Base.BaseActivity
import com.example.weatherapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class MainActivity : BaseActivity<MainActivityViewModel>() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        obtainLocation()

    }

    @SuppressLint("MissingPermission")
    private fun obtainLocation(){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                  location?.latitude
//                longitude = location?.longitude
            }
    }

    private fun permissionsToRequest(wantedPermissions: ArrayList<String>): ArrayList<String>? {
        val result: ArrayList<String> = ArrayList()
        for (perm in wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm)
            }
        }
        return result
    }

    private fun hasPermission(permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    override fun layoutRes(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getViewModel(): MainActivityViewModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
