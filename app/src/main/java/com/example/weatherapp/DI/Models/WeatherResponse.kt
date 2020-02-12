package com.example.weatherapp.DI.Models

data class WeatherResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Data>,
    val message: Int
)