package com.example.weatherapp.fragments.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.current_weather_item.view.*
import kotlinx.android.synthetic.main.daily_weather_item.view.*
import kotlinx.android.synthetic.main.weather_condition.view.*

class DailyViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {
    val date: TextView = view.daily_date_text
    val image: ImageView = view.daily_weather_icon
    val description: TextView = view.daily_description_text
    val minTemp: TextView = view.min_temp
    val maxTemp: TextView = view.max_temp
//    val listListener = view.setOnClickListener {
//        onItemListener.onClickItem(adapterPosition)
//    }
}

class CurrentWeatherViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {
    val city: TextView = view.city_text
    val date: TextView = view.current_date_text
    val image: ImageView = view.current_weather_icon
    val description: TextView = view.current_weather_description_text
    val temp: TextView = view.current_weather_temp
}