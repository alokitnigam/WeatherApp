package com.example.weatherapp.DI.Models

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


data class Data(
    val clouds: Clouds,
    val dt: Int,
    val dt_txt: String,
    val main: Main,
    val sys: Sys,
    val weather: List<Weather>,
    val wind: Wind


)

