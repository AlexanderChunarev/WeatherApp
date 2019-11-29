package com.example.weatherapp.extentions

import android.os.Build
import androidx.annotation.RequiresApi
import retrofit2.Call
import retrofit2.HttpException
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


fun <T> Call<T>.await(): T {
    val response = execute()
    return when {
        response.isSuccessful -> response.body()!!
        else -> throw HttpException(response)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun Long.convertToDate(pattern: String): String? {
    return SimpleDateFormat(pattern, Locale.ENGLISH).format(Date(this * 1000))
}