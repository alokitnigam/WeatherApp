package com.example.weatherapp.Views

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.weatherapp.Base.BaseActivity
import com.example.weatherapp.DI.Models.Data
import com.example.weatherapp.DI.Network.NetworkState
import com.example.weatherapp.R
import com.example.weatherapp.Utils.Utilities
import com.example.weatherapp.Utils.Utilities.Companion.kelvinToCelsius
import com.example.weatherapp.Views.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.date
import kotlinx.android.synthetic.main.bottom_sheet_content_view.*
import kotlinx.android.synthetic.main.forecast_item.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : BaseActivity<MainActivityViewModel>() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var bottomSheetBehavior: BottomSheetBehavior<CardView>

    @Inject
    lateinit var mainActivityViewModel: MainActivityViewModel

    @Inject
    lateinit var forecastAdapter: ForecastAdapter

    private val MY_PERMISSIONS_REQUEST_LOCATION =2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recyclerview.adapter = forecastAdapter

        recyclerview.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        if(checkLocationPermission()){
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
            showError()
        }else{
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            obtainLocation()
        }

        setUpBottomSheet()
        setUpObservers()
        setUpListeners()

    }

    private fun setUpListeners() {
        retry.setOnClickListener {
            checkLocationPermission()
        }
    }

    private fun setUpBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(card)
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    drag_me.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@MainActivity,
                            R.drawable.ic_keyboard_arrow_up_black_24dp
                        )
                    )
                } else {
                    drag_me.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@MainActivity,
                            R.drawable.ic_keyboard_arrow_down_black_24dp
                        )
                    )
                }
            }

        })
    }

    private fun setUpObservers() {
        getViewModel().weatherData.observe(this, Observer {
            populateUI(it)
            weatherimage.visibility = View.VISIBLE
            date.visibility = View.VISIBLE
            feelslike.visibility = View.VISIBLE
            temperature.visibility = View.VISIBLE
            card.visibility = View.VISIBLE

        })

        getViewModel().netWorkResponse.observe(this, Observer {
            if (it.status== NetworkState.FAILED) {
                showError()
            }else{
                coordinator.setBackgroundDrawable(
                    ContextCompat.getDrawable(this,R.drawable.background)
                )
                retry.visibility = View.GONE
                constraint.visibility = View.VISIBLE
                card.visibility = View.VISIBLE
            }
        })
    }

    private fun populateUI(it: List<Data>) {
        val df = DecimalFormat("#.##")
        val formattedtemp = df.format(kelvinToCelsius(it[0].main.temp))
//        val sb = SpannableStringBuilder("$formattedtemp \u00B0")
//        val superscriptSpan = SuperscriptSpan()
//        val relativeSizeSpan = RelativeSizeSpan(.25f)
//        sb.setSpan(relativeSizeSpan,7,sb.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        sb.setSpan(superscriptSpan,7,sb.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        temperature.text = "$formattedtemp \u00B0"
        date.text = Utilities.getDate(it[0].dt.toLong())
        feelslike.text = "Feels like "+df.format(kelvinToCelsius(it[0].main.feels_like))+" °"
        val weatherDrawable:Int = Utilities.getWeatherDrawable(it[0].weather[0].main)
        weatherimage.setImageDrawable(ContextCompat.getDrawable(this,weatherDrawable))

        val weatherList: HashMap<String,ArrayList<Data>> = HashMap()

        val dateFormat = SimpleDateFormat("dd/MM/yy")

        for (i in it.indices){
            val currentdate = Date(it[i].dt.toLong()*1000)


            if(!weatherList.containsKey(dateFormat.format(currentdate))){
                weatherList[dateFormat.format(currentdate)] = ArrayList()
            }
            weatherList[dateFormat.format(currentdate)]!!.add(it[i])
        }

        val treeMap: TreeMap<String, List<Data>> = TreeMap(weatherList)


        val filteredList = ArrayList<Data>()
        for(key in treeMap.keys){
            filteredList.add(treeMap[key]!![0])
        }


        forecastAdapter.setList(filteredList)




    }

    @SuppressLint("MissingPermission")
    private fun obtainLocation(){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->


                getViewModel().getWeather(location!!.latitude.toString(),location.longitude.toString())
            }
    }



    override fun layoutRes(): Int {
       return R.layout.activity_main
    }



    override fun getViewModel(): MainActivityViewModel {
        return mainActivityViewModel
    }


    private fun checkLocationPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder(this)
                    .setTitle(R.string.title_location_permission)
                    .setMessage(R.string.text_location_permission)
                    .setPositiveButton(R.string.ok
                    ) { _, i ->
                        ActivityCompat.requestPermissions(
                            this@MainActivity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION
                        )
                    }
                    .create()
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
         MY_PERMISSIONS_REQUEST_LOCATION -> {
            if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                    obtainLocation()

                }

            } else {

                showError()

            }
            return;
        }

    }
    }

    private fun showError() {

        retry.visibility = View.VISIBLE
        weatherimage.visibility = View.GONE
        date.visibility = View.GONE
        feelslike.visibility = View.GONE
        temperature.visibility = View.GONE
        card.visibility = View.GONE

    }

}
