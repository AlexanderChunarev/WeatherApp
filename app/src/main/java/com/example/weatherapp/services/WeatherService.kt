package com.example.weatherapp.services

import com.example.weatherapp.entities.WeatherList
import com.example.weatherapp.responses.CurrentWeatherResponse
import com.example.weatherapp.responses.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/forecast?")
    fun getForecast(@Query("q") id: String, @Query("units") units: String): Call<WeatherList>

    @GET("data/2.5/weather?")
    fun getCurrentForecast(@Query("q") id: String, @Query("units") units: String): Call<CurrentWeatherResponse>
}