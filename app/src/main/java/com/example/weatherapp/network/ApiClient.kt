package com.example.weatherapp.network

import com.example.weatherapp.services.WeatherService
import retrofit2.Retrofit

class ApiClient(retrofit: Retrofit) {

    val weatherService: WeatherService = retrofit.create(WeatherService::class.java)
}