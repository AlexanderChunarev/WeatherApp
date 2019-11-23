package com.example.weatherapp.responses

import com.example.weatherapp.entities.Weather
import com.example.weatherapp.entities.WeatherInfo
import com.example.weatherapp.entities.Wind
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class WeatherResponse(
    val id: Long,
    val main: WeatherInfo,
    val weather: List<Weather>,
    val wind: Wind,
    val rain: Map<String, Int>?,
    val clouds: Map<String, Int>?
)