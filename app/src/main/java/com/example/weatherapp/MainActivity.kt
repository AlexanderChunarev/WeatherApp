package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.weatherapp.configuration.APIConfiguration
import com.example.weatherapp.interceptors.ServiceBuilder
import com.example.weatherapp.responses.WeatherResponse
import com.example.weatherapp.services.WeatherService

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var weatherData: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherData = findViewById(R.id.text_forecast)

        findViewById<View>(R.id.get_forecast).setOnClickListener { getCurrentData() }
    }

    private fun getCurrentData() {
        val weatherService = ServiceBuilder().buildService(WeatherService::class.java)
        val call = weatherService.getCurrentWeatherData(APIConfiguration.COUNTRY_ID)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.code() == SUCCESS) {
                    val weatherResponse = response.body()!!
                    val stringBuilder =
                        "Wind speed: " +
                                weatherResponse.wind.speed +
                                "\n" +
                        "Temperature: " +
                            weatherResponse.main.temp +
                            "\n" +
                            "Humidity: " +
                            weatherResponse.main.humidity +
                            "\n" +
                            "Pressure: " +
                            weatherResponse.main.pressure

                    weatherData!!.text = stringBuilder
                }
            }
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "FAIL", Toast.LENGTH_LONG).show()
                weatherData!!.text = t.message
            }
        })
    }

    companion object {
        const val SUCCESS = 200
    }

}
