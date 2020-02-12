package com.example.weatherapp.Views

import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {
    @Provides
    fun provideForecastAdapter():ForecastAdapter{
        return ForecastAdapter()
    }
}