package com.example.weatherapp.entities

import com.example.weatherapp.responses.WeatherResponse
import com.squareup.moshi.Json

data class WeatherList(
    @Json(name = "list")val list: List<WeatherResponse>)