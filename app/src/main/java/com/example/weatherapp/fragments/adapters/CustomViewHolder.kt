package com.example.weatherapp.fragments.adapters

import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.extentions.convertToDate
import com.example.weatherapp.responses.CurrentWeatherResponse
import com.example.weatherapp.responses.Response
import com.example.weatherapp.responses.WeatherResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.current_weather_item.view.*
import kotlinx.android.synthetic.main.daily_weather_item.view.*
import kotlinx.android.synthetic.main.weather_condition.view.*

class DailyViewHolder(view: View) : ViewHolder(view) {
    private val date: TextView = view.daily_date_text
    private val image: ImageView = view.daily_weather_icon
    private val description: TextView = view.daily_description_text
    private val minTemp: TextView = view.min_temp
    private val maxTemp: TextView = view.max_temp
    //    val listListener = view.setOnClickListener {
//        onItemListener.onClickItem(adapterPosition)
//    }

    override fun bind(response: Response) {
        (response as WeatherResponse).apply {
            description.text = weather[0].description
            minTemp.text = main.temp_min
            maxTemp.text = main.temp_max
            Picasso.get()
                .load("${DataAdapter.ICON_URL}${weather[0].icon}${DataAdapter.ICON_FORMAT}")
                .fit()
                .into(image)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                date.text = dt.convertToDate(DataAdapter.DATE_FORMAT)
            }
        }
    }
}

class CurrentWeatherViewHolder(view: View) : ViewHolder(view) {
    private val city: TextView = view.city_text
    private val date: TextView = view.current_date_text
    private val image: ImageView = view.current_weather_icon
    private val description: TextView = view.current_weather_description_text
    private val temp: TextView = view.current_weather_temp

    override fun bind(response: Response) {
        (response as CurrentWeatherResponse).apply {
            city.text = name
            temp.text = main.temp
            description.text = weather[0].description
            Picasso.get()
                .load("${DataAdapter.ICON_URL}${weather[0].icon}${DataAdapter.ICON_FORMAT}")
                .fit().into(image)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                date.text = dt.convertToDate(DataAdapter.DATE_FORMAT)
            }
        }
    }
}