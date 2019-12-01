package com.example.weatherapp.extentions

import retrofit2.Call
import retrofit2.HttpException

fun <T> Call<T>.await(): T {
    val response = execute()
    return when {
        response.isSuccessful -> response.body()!!
        else -> throw HttpException(response)
    }
}