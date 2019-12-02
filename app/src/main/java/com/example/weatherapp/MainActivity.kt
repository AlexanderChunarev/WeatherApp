package com.example.weatherapp

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.thirdhomework.listener.OnItemListener
import com.example.weatherapp.fragments.InfoFragment
import com.example.weatherapp.fragments.ListFragment
import com.example.weatherapp.fragments.adapters.DataAdapter
import com.example.weatherapp.responses.Response
import com.example.weatherapp.responses.WeatherResponse
import com.example.weatherapp.viewmodels.ViewModelFactory
import com.example.weatherapp.viewmodels.WeatherViewModel

class MainActivity : AppCompatActivity(), OnItemListener {
    private val adapter by lazy { DataAdapter(this) }
    private val listFragment by lazy { ListFragment() }
    private var responseInfo: Response? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clearBackStack()
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
                initRecyclerView()
            })
        }
    }

    private fun initRecyclerView() {
        listFragment.newAdapter = adapter
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, listFragment)
            .commit()
    }

    private fun switchFragment(response: Response) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.slide_in_left,
                0,
                0,
                android.R.anim.slide_out_right
            )
            .replace(R.id.fragment_container, InfoFragment.newInstance(response as WeatherResponse))
            .addToBackStack(null)
            .commit()
    }

    override fun onClickItem(position: Int) {
        responseInfo = adapter.getWeatherList()[position]
        switchFragment(responseInfo!!)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (responseInfo != null) {
            outState.putSerializable(RESPONSE_KEY, responseInfo)
        }
    }

    private fun clearBackStack() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    companion object {
        const val RESPONSE_KEY = "response_key"
    }
}
