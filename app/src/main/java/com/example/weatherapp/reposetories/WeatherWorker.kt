package com.example.weatherapp.reposetories

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherapp.builder.ServiceBuilder
import com.example.weatherapp.configuration.APIConfiguration
import com.example.weatherapp.extentions.await
import com.example.weatherapp.responses.CurrentWeatherResponse
import com.example.weatherapp.responses.WeatherResponse
import com.example.weatherapp.services.WeatherService


class WeatherWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        ServiceBuilder().buildService(WeatherService::class.java).apply {
            getForecast(APIConfiguration.COUNTRY_ID).await().apply {
                forecast = this.list
            }
            getCurrentForecast(APIConfiguration.COUNTRY_ID).await().apply {
                currForecast = this
            }
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