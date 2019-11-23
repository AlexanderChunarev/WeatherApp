package com.example.weatherapp.services

import com.example.weatherapp.responses.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/weather?")
    fun getCurrentWeatherData(@Query("id") id: String): Call<WeatherResponse>
}