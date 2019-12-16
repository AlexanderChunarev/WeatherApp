package com.example.weatherapp.reposetories

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherapp.cache.SPCache
import com.example.weatherapp.builder.ServiceBuilder
import com.example.weatherapp.extentions.await
import com.example.weatherapp.responses.CurrentWeatherResponse
import com.example.weatherapp.responses.WeatherResponse
import com.example.weatherapp.services.WeatherService
import com.example.weatherapp.viewmodels.WeatherViewModel
import retrofit2.HttpException


class WeatherWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        val unit = inputData.getString(WeatherViewModel.UNITS)!!
        try {
            ServiceBuilder().buildService(WeatherService::class.java).apply {
                getCurrentForecast(SPCache.COUNTRY_NAME, unit).await().apply {
                    currForecast = this
                }
                getForecast(SPCache.COUNTRY_NAME, unit).await().apply {
                    forecast = this.list
                }
            }
        } catch (e: HttpException) {
            return Result.failure()
        }
        return Result.success()
    }

    companion object {
        fun fetchForecast() = forecast
        fun fetchCurrentForecast() = currForecast
        private var forecast: List<WeatherResponse>? = null
        private var currForecast: CurrentWeatherResponse? = null
    }
}