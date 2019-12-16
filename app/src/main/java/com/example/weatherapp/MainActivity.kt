package com.example.weatherapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.thirdhomework.listener.OnItemListener
import com.example.weatherapp.fragments.InfoFragment
import com.example.weatherapp.fragments.ListFragment
import com.example.weatherapp.fragments.SettingsFragment
import com.example.weatherapp.fragments.adapters.DataAdapter
import com.example.weatherapp.responses.Response
import com.example.weatherapp.responses.WeatherResponse
import com.example.weatherapp.viewmodels.ViewModelFactory
import com.example.weatherapp.viewmodels.WeatherViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnItemListener, SettingsFragment.UnitCallBack {
    private val adapter by lazy { DataAdapter(this) }
    private val weatherViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory(this, this))
            .get(WeatherViewModel::class.java)
    }
    private var response: Response? = null
    private var currentUnit: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        clearBackStack()
        swipe_refresh_layout.setOnRefreshListener {
            adapter.clear()
            weatherViewModel.loadWeatherData()
        }
        observeData()
    }

    private fun observeData() {
        weatherViewModel.apply {
            currentWeatherForecast.observe(this@MainActivity, Observer { weather ->
                adapter.addItem(weather)
            })
            forecastList.observe(this@MainActivity, Observer { weather ->
                weather.forEach {
                    adapter.addItem(it)
                }
                swipe_refresh_layout.isRefreshing = false
            })
            currentUnit = spCache.unit
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val listFragment = ListFragment.newInstance()
        listFragment.newAdapter = adapter
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, listFragment)
            .commit()
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.slide_in_left,
                0,
                0,
                android.R.anim.slide_out_right
            )
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onClickItem(position: Int) {
        response = adapter.getWeatherList()[position]
        switchFragment(InfoFragment.newInstance(response as WeatherResponse))
    }

    private fun clearBackStack() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings_btn) {
            clearBackStack()
            switchFragment(SettingsFragment.newInstance(weatherViewModel.spCache.unit))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setUnit(s: String?) {
        adapter.clear()
        weatherViewModel.apply {
            spCache.unit = s!!
            loadWeatherData()
        }
    }
}