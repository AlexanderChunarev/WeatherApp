package com.example.weatherapp.callbacks

import com.example.weatherapp.responses.WeatherResponse

interface WeatherCallback {
    fun getWeatherInfo(weatherResponse: WeatherResponse)
}