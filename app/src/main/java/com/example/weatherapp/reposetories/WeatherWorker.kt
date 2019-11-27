package com.example.weatherapp.reposetories

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherapp.builder.ServiceBuilder
import com.example.weatherapp.configuration.APIConfiguration
import com.example.weatherapp.extentions.await
import com.example.weatherapp.responses.WeatherResponse
import com.example.weatherapp.services.WeatherService


class WeatherWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        ServiceBuilder().buildService(WeatherService::class.java)
            .getCurrentWeatherData(APIConfiguration.COUNTRY_ID).await().apply {
                weatherResponse = this.list
            }
        return Result.success()
    }

    companion object {
        fun fetchForecast() = weatherResponse
        private var weatherResponse: List<WeatherResponse>? = null
    }
}