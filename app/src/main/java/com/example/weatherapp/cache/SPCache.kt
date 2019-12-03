package com.example.weatherapp.cache

import android.content.Context
import android.content.SharedPreferences

class SPCache(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(SHARED_FILE, MODE)

    var unit: String = prefs.getString(SELECTED_SYSTEM, DEFAULT_METRIC)!!
        set(value) {
            if (isValid(value)) {
                prefs.edit().putString(SELECTED_SYSTEM, value).apply()
                field = value
            }
        }

    private fun isValid(value: String) = value == "metric" || value == "imperial"

    companion object {
        const val MODE = 0
        const val SHARED_FILE = "settings_data"
        const val COUNTRY_NAME = "Cherkasy"
        const val SELECTED_SYSTEM = "metric_or_imperial"
        const val DEFAULT_METRIC = "metric"
    }
}