package  com.example.weatherapp.DI.Modules


import com.example.weatherapp.Views.MainActivity
import com.example.weatherapp.Views.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributesMainActivity(): MainActivity


}