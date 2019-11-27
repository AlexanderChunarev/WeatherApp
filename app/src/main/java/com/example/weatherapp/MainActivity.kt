package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.weatherapp.reposetories.WeatherWorker
import com.example.weatherapp.responses.WeatherResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Toast
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.get_forecast).setOnClickListener { getCurrentData() }
    }

    private fun getCurrentData() {
        val request = OneTimeWorkRequest.Builder(WeatherWorker::class.java)
            .build()
        WorkManager.getInstance(this).enqueue(request)
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.id).observe(this, Observer {
            if (it.state.isFinished) {
                WeatherWorker.fetchForecast()!!.apply {
                    Toast.makeText(this@MainActivity, this[7].dt.toString(), Toast.LENGTH_LONG).show()

//                    this.forEach { weather ->
//                        run {
//                            TimeUnit.SECONDS.sleep(3)
//                            val stringBuilder =
//                                "Wind speed: " +
//                                        weather.wind.speed +
//                                        "\n" +
//                                        "Temperature: " +
//                                        weather.main.temp +
//                                        "\n" +
//                                        "Humidity: " +
//                                        weather.main.humidity +
//                                        "\n" +
//                                        "dt: " +
//                                        weather.dt
//                            text_forecast.text = stringBuilder
//                        }
//                    }

                }
            }
        })
    }

    companion object {
        const val SUCCESS = 200
    }

}
