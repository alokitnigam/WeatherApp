package com.example.weatherapp.Base

import android.os.Bundle

import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.lifecycle.ViewModel

import dagger.android.support.DaggerAppCompatActivity


abstract class BaseActivity<T : ViewModel>  : DaggerAppCompatActivity() {

    private var viewModel: T? = null


    @LayoutRes
    protected abstract fun layoutRes(): Int

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes())
        this.viewModel = viewModel ?: getViewModel()

    }

    abstract fun getViewModel(): T

}