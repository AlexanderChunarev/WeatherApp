package com.example.weatherapp.data

import android.provider.BaseColumns

object DBContract {
    object WeatherEntry : BaseColumns {
        const val WEATHER_TABLE = "weather"
        const val TIME = "dt"
        const val CITY_NAME = "name"
        const val WEATHER_MAIN_CONDITION = "main"
        const val DESCRIPTION = "description"
        const val ICON = "icon"
        const val TEMPERATURE = "temperature"
        const val PRESSURE = "pressure"
        const val HUMIDITY = "humidity"
        const val TEMPERATURE_MAX = "temp_max"
        const val TEMPERATURE_MIN = "temp_min"
        const val WIND_SPEED = "speed"
        const val WIND_DEGREE = "deg"
    }
}

const val CREATE_WEATHER_TABLE =
    "create table ${DBContract.WeatherEntry.WEATHER_TABLE} (" +
            "${BaseColumns._ID} integer primary key," +
            "${DBContract.WeatherEntry.TIME} numeric," +
            "${DBContract.WeatherEntry.CITY_NAME} text," +
            "${DBContract.WeatherEntry.WEATHER_MAIN_CONDITION} text," +
            "${DBContract.WeatherEntry.DESCRIPTION} text," +
            "${DBContract.WeatherEntry.ICON} text," +
            "${DBContract.WeatherEntry.TEMPERATURE} real," +
            "${DBContract.WeatherEntry.TEMPERATURE_MIN} real," +
            "${DBContract.WeatherEntry.TEMPERATURE_MAX} real," +
            "${DBContract.WeatherEntry.PRESSURE} real," +
            "${DBContract.WeatherEntry.HUMIDITY} real," +
            "${DBContract.WeatherEntry.WIND_SPEED} real," +
            "${DBContract.WeatherEntry.WIND_DEGREE} real)"