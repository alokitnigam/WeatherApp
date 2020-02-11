package com.example.weatherapp.DI.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Weather(
    val description: String,
    val icon: String,
    @PrimaryKey
    val id: Int,
    val main: String
)