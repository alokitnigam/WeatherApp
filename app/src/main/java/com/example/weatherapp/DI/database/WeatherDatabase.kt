package com.example.weatherapp.DI.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.perpuletask.DI.database.WeatherRepo
import com.example.weatherapp.DI.Models.Weather

@Database(entities = [Weather::class], version = 1,exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherRepo

    companion object {
        val DATABASE_NAME = "weather.db"
    }


}
