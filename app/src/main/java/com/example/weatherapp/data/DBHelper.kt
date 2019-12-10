package com.example.weatherapp.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(
    context: Context?
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.apply {
            execSQL(CREATE_WEATHER_TABLE)
            execSQL(CREATE_MAIN_INFO_TABLE)
            execSQL(CREATE_WIND_TABLE)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "persons"
        const val DATABASE_VERSION = 3
        const val ID_KEY = "_id"

        const val WEATHER_TABLE = "weather"
        const val TIME_KEY = "dt"
        const val NAME_KEY = "name"
        const val CREATE_WEATHER_TABLE =
            "create TABLE $WEATHER_TABLE($ID_KEY integer primary key,$TIME_KEY text,$NAME_KEY text)"

        const val MAIN_INFO_TABLE = "main"
        const val TEMPERATURE = "temperature"
        const val PRESSURE = "pressure"
        const val HUMIDITY = "humidity"
        const val TEMPERATURE_MAX = "temp_max"
        const val TEMPERATURE_MIN = "temp_min"
        const val CREATE_MAIN_INFO_TABLE =
            "create TABLE $MAIN_INFO_TABLE($ID_KEY integer primary key,$TEMPERATURE text,$PRESSURE text,$HUMIDITY text,$TEMPERATURE_MAX text,$TEMPERATURE_MIN text)"

        const val WIND_TABLE = "wind"
        const val SPEED = "speed"
        const val DEGREE = "deg"
        const val CREATE_WIND_TABLE =
            "create TABLE $WIND_TABLE($ID_KEY integer primary key,$SPEED text,$DEGREE text)"
    }
}