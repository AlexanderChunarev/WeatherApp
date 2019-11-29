package com.example.weatherapp

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.weatherapp.fragments.adapters.DataAdapter
import com.example.weatherapp.viewmodels.ViewModelFactory
import com.example.weatherapp.viewmodels.WeatherViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class MainActivity : AppCompatActivity() {
    private val adapter by lazy { DataAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list_of_cpu.adapter = adapter
        getCurrentData()
    }

    private fun getCurrentData() {
        val weatherViewModel =
            ViewModelProviders.of(this, ViewModelFactory(this, this))
                .get(WeatherViewModel::class.java)

        weatherViewModel.apply {
            currentWeatherForecast.observe(this@MainActivity, Observer { weather ->
                adapter.addItem(weather)
            })
            forecastList.observe(this@MainActivity, Observer { weather ->
                weather.forEach {
                    adapter.addItem(it)
                }
            })
        }
    }

    companion object {
        const val DATE_FORMAT = "MM dd"
    }

}
