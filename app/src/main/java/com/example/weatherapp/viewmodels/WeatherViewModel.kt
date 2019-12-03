package com.example.weatherapp.viewmodels

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.weatherapp.cache.SPCache
import com.example.weatherapp.reposetories.WeatherWorker
import com.example.weatherapp.responses.CurrentWeatherResponse
import com.example.weatherapp.responses.WeatherResponse

class WeatherViewModel(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) : ViewModel() {
    var forecastList: MutableLiveData<List<WeatherResponse>> = MutableLiveData()
    var currentWeatherForecast: MutableLiveData<CurrentWeatherResponse> = MutableLiveData()
    val spCache by lazy { SPCache(context) }
    var location = spCache.unit

    init {
        loadWeatherData()
    }

    private fun createInputData(): Data {
        return Data.Builder()
            .putString(UNITS, location)
            .build()
    }

    private fun loadWeatherData() {
        val request = OneTimeWorkRequest.Builder(WeatherWorker::class.java)
            .setInputData(createInputData())
            .build()
        WorkManager.getInstance(context).enqueue(request)
        WorkManager.getInstance(context).getWorkInfoByIdLiveData(request.id)
            .observe(lifecycleOwner, Observer {
                if (it.state.isFinished) {
                    WeatherWorker.apply {
                        currentWeatherForecast.value = fetchCurrentForecast()
                        forecastList.value = fetchForecast()
                    }
                }
            })

    }

    companion object {
        const val UNITS = "selected_unit"
    }
}