package com.example.weatherapp.responses

import com.example.weatherapp.entities.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
class WeatherResponse(
    @Json(name = "dt") val dt: Long,
    @Json(name = "main") val main: Main,
    @Json(name = "weather") val weather: List<Weather>,
    @Json(name = "wind") val wind: Wind
) : Response()