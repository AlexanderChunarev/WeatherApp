package com.example.weatherapp.reposetories

import com.example.weatherapp.configuration.APIConfiguration
import com.example.weatherapp.services.WeatherService

class ForecastRepository(private val weatherService: WeatherService) {
    fun set() {
        weatherService.getCurrentWeatherData(APIConfiguration.COUNTRY_ID).execute()
    }
}