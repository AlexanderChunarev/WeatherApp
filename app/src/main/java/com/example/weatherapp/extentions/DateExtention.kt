package com.example.weatherapp.extentions

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun Long.convertToDate(pattern: String): String? {
    return SimpleDateFormat(pattern, Locale.ENGLISH).format(Date(this * 1000))
}