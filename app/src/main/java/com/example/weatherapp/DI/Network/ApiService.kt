package com.example.weatherapp.DI.Network

import com.example.weatherapp.DI.Models.WeatherResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {


    @GET("data/2.5/weather?")
    fun getCurrentWeatherData(@Query("lat") lat: String?, @Query("lon") lon: String?,
                              @Query("appid") app_id: String?): Single<Response<WeatherResponse>>

    @GET("data/2.5/forecast?")
    fun getForecast(@Query("lat") lat: String?, @Query("lon") lon: String?,@Query("cnt") cnt:String,
                    @Query("appid") app_id: String?): Single<Response<WeatherResponse>>

}

