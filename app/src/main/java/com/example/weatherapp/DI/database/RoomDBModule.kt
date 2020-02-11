package com.example.perpuletask.DI.database

import android.content.Context

import androidx.room.Room
import com.example.weatherapp.DI.database.WeatherDatabase

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
class RoomDBModule {

    @Singleton
    @Provides
    fun provideAudioDatabase(context: Context): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java, WeatherDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Singleton
    @Provides
    fun provideAudioDao(weatherDatabase: WeatherDatabase): WeatherRepo {
        return weatherDatabase.audioDao()
    }
}