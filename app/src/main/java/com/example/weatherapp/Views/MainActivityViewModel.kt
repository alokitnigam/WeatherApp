package com.example.weatherapp.Views

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.DI.Models.Data
import com.example.weatherapp.DI.Models.WeatherResponse
import com.example.weatherapp.DI.Network.ApiService
import com.example.weatherapp.DI.Network.NetworkModule
import com.example.weatherapp.DI.Network.NetworkState
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import retrofit2.Response
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(val apiService: ApiService) : ViewModel()
{

    var weatherData : MutableLiveData<List<Data>> = MutableLiveData()
    var netWorkResponse: MutableLiveData<NetworkState> = MutableLiveData()
    var compositeDisposable = CompositeDisposable()

    fun getWeather(lat:String,lon:String){


        apiService.getForecast(lat,lon,"40",NetworkModule.API_KEY)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .subscribe(object :SingleObserver<Response<WeatherResponse>>{
                override fun onSuccess(t:Response<WeatherResponse> ) {
                    Log.i("","")
                    if (t.body()!=null){

                        weatherData.postValue(t.body()!!.list)

                        netWorkResponse.value = NetworkState(NetworkState.SUCESS,"Success")

                    }else{
                        netWorkResponse.value = NetworkState(NetworkState.FAILED,"Failed")

                    }
                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    netWorkResponse.value = NetworkState(NetworkState.FAILED,e.localizedMessage)

                }
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}