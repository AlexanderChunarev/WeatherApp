package com.example.weatherapp.entities

import com.squareup.moshi.Json

data class WeatherInfo(
    val temp: Double,
    val humidity: Double,
    val pressure: Double,
    @Json(name = "temp_min") val tempMin: Double,
    @Json(name = "temp_max") val tempMax: Double
)