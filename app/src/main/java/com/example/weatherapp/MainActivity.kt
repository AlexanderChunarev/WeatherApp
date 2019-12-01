package com.example.weatherapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thirdhomework.listener.OnItemListener
import com.example.weatherapp.fragments.adapters.DataAdapter
import com.example.weatherapp.viewmodels.ViewModelFactory
import com.example.weatherapp.viewmodels.WeatherViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class MainActivity : AppCompatActivity(), OnItemListener {
    private val adapter by lazy { DataAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list_of_cpu.adapter = adapter
        list_of_cpu.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
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

    override fun onClickItem(position: Int) {
       Toast.makeText(this, "CLICKED", Toast.LENGTH_LONG).show()
    }

}
