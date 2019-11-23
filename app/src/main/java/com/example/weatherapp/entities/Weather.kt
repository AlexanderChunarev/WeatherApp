package com.example.weatherapp.entities

data class Weather(
    val id: Long,
    val main: String,
    val description: String,
    val icon: String
)