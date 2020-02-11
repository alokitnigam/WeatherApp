package com.example.weatherapp.DI.VMFactory

import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.DI.VMFactory.DaggerViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory
}