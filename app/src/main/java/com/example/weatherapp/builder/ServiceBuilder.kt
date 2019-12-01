package com.example.weatherapp.builder

import com.example.weatherapp.configuration.APIConfiguration
import com.example.weatherapp.services.WeatherService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


class ServiceBuilder {

    private val interceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            return chain.proceed(
                chain.request().newBuilder().url(
                    chain.request().url.newBuilder()
                        .addQueryParameter(QUERY_PARAM, APIConfiguration.API_KEY)
                        .build()
                ).build()
            )
        }

    }

    private val okHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(interceptor)
        addInterceptor(
            HttpLoggingInterceptor().apply {
                connectTimeout(30, TimeUnit.SECONDS)
                readTimeout(30, TimeUnit.SECONDS)
                level = HttpLoggingInterceptor.Level.BODY
            })
    }

    fun buildService(service: Class<WeatherService>): WeatherService {
        return Retrofit.Builder()
            .baseUrl(APIConfiguration.BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(
                        KotlinJsonAdapterFactory()
                    ).build()
                )
            )
            .client(okHttpClient.build())
            .build()
            .create(service)
    }

    companion object {
        const val QUERY_PARAM = "appid"
        const val QUERY_SYSTEM = "units"
    }

}