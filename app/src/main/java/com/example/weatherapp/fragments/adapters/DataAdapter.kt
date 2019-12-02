package com.example.weatherapp.fragments.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.thirdhomework.listener.OnItemListener
import com.example.weatherapp.R
import com.example.weatherapp.responses.CurrentWeatherResponse
import com.example.weatherapp.responses.Response
import com.example.weatherapp.responses.WeatherResponse

class DataAdapter(private val onItemListener: OnItemListener) : Adapter<RecyclerView.ViewHolder>() {
    private val weatherList = mutableListOf<Response>()

    fun getWeatherList() = weatherList

    fun addItem(response: Response) {
        weatherList.add(response)
        notifyItemInserted(weatherList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        when (viewType) {
            TYPE_CURRENT_WEATHER -> {
                viewHolder = CurrentWeatherViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.current_weather_item,
                        parent,
                        false
                    )
                )
            }
            TYPE_DAILY_WEATHER -> {
                viewHolder = DailyViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.daily_weather_item,
                        parent,
                        false
                    ), onItemListener
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
                    bind(currentWeather)
                }
            }
            TYPE_DAILY_WEATHER -> {
                val dailyWeather = weatherList[position] as WeatherResponse
                (holder as DailyViewHolder).apply {
                    bind(dailyWeather)
                }
            }
        }
    }

    companion object {
        const val TYPE_CURRENT_WEATHER = 1
        const val TYPE_DAILY_WEATHER = 2
        const val ICON_URL = "http://openweathermap.org/img/wn/"
        const val ICON_FORMAT = "@2x.png"
        const val DATE_FORMAT = "dd.MM.yyyy hh:mma"
        //const val DATE_FORMAT = "dd.MM.yyyy"
        const val DAY_UNIX = 86400
    }
}

