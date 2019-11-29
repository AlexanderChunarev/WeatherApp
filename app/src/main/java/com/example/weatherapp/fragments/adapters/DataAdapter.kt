package com.example.weatherapp.fragments.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.weatherapp.R
import com.example.weatherapp.responses.CurrentWeatherResponse
import com.example.weatherapp.responses.WeatherResponse
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


class DataAdapter() : Adapter<RecyclerView.ViewHolder>() {
    private val weatherList = mutableListOf<Any>()

    fun addItem(any: Any) {
        weatherList.add(any)
        notifyItemInserted(weatherList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD")
        when (viewType) {
            TYPE_CURRENT_WEATHER -> {
                viewHolder = CurrentWeatherViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.cw,
                        parent,
                        false
                    )
                )
            }
            TYPE_DAILY_WEATHER -> {
                viewHolder = DailyViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.dw,
                        parent,
                        false
                    )
                )
            }
        }
        return viewHolder!!
    }

    override fun getItemViewType(position: Int): Int {
        if (weatherList[position] is CurrentWeatherResponse) {
            return TYPE_CURRENT_WEATHER
        } else if (weatherList[position] is WeatherResponse) {
            return TYPE_DAILY_WEATHER
        }
        return -1
    }

    override fun getItemCount() = weatherList.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_CURRENT_WEATHER -> {
                val currentWeather = weatherList[position] as CurrentWeatherResponse
                (holder as CurrentWeatherViewHolder).apply {
                    city.text = currentWeather.name
                    date.text = currentWeather.dt.convertToDate()
                    temp.text = currentWeather.main.temp
                    description.text = currentWeather.weather[0].description
                    Picasso.get().load("$ICON_URL${currentWeather.weather[0].icon}$ICON_FORMAT")
                        .fit().into(image)
                }
            }
            TYPE_DAILY_WEATHER -> {
                val dailyWeather = weatherList[position] as WeatherResponse
                (holder as DailyViewHolder).apply {
                    date.text = dailyWeather.dt.convertToDate()
                    description.text = dailyWeather.weather[0].description
                    minTemp.text = dailyWeather.main.temp_min
                    maxTemp.text = dailyWeather.main.temp_max
                    Picasso.get().load("$ICON_URL${dailyWeather.weather[0].icon}$ICON_FORMAT").fit()
                        .into(image)
                }
            }
        }
    }

    companion object {
        const val TYPE_CURRENT_WEATHER = 1
        const val TYPE_DAILY_WEATHER = 2
        const val ICON_URL = "http://openweathermap.org/img/wn/"
        const val ICON_FORMAT = "@2x.png"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun Long.convertToDate(): String? {
    return DateTimeFormatter.ISO_INSTANT
        .format(java.time.Instant.ofEpochSecond(this)).toString()
}

