package com.example.weatherapp.viewmodels

import android.content.ContentValues
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.*
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.weatherapp.cache.SPCache
import com.example.weatherapp.data.CLEAR_TABLE
import com.example.weatherapp.data.DBContract
import com.example.weatherapp.data.DBHelper
import com.example.weatherapp.data.SELECT_ALL_FROM_WEATHER_TABLE
import com.example.weatherapp.entities.Main
import com.example.weatherapp.entities.Weather
import com.example.weatherapp.entities.Wind
import com.example.weatherapp.reposetories.WeatherWorker
import com.example.weatherapp.responses.CurrentWeatherResponse
import com.example.weatherapp.responses.WeatherResponse

class WeatherViewModel(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner

) : ViewModel() {
    var forecastList: MutableLiveData<List<WeatherResponse>> = MutableLiveData()
    var currentWeatherForecast: MutableLiveData<CurrentWeatherResponse> = MutableLiveData()
    val spCache by lazy { SPCache(context) }
    private val dbHelper by lazy { DBHelper(context) }
    private val db by lazy { dbHelper.writableDatabase }
    private val cv by lazy { ContentValues() }

    init {
        loadWeatherData()
    }

    private fun createInputData(): Data {
        return Data.Builder()
            .putString(UNITS, spCache.unit)
            .build()
    }

    fun loadWeatherData() {
        when {
            checkConnection() -> {
                loadDataFromWeb()
            }
            else -> {
                loadDataFromDB()
            }
        }
    }

    fun loadDataFromWeb() {
        val request by lazy {
            OneTimeWorkRequest.Builder(WeatherWorker::class.java)
                .setInputData(createInputData())
                .build()
        }
        WorkManager.getInstance(context).enqueue(request)
        WorkManager.getInstance(context).getWorkInfoByIdLiveData(request.id)
            .observe(lifecycleOwner, Observer {
                if (it.state.isFinished) {
                    WeatherWorker.apply {
                        currentWeatherForecast.value = fetchCurrentForecast()
                        forecastList.value = fetchForecast()

                        writeToDB(forecastList.value!!)
                    }
                }
            })

    }

    private fun checkConnection(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetwork != null
    }

    private fun clearTable() {
        db.execSQL(CLEAR_TABLE)
    }

    private fun writeToDB(list: List<WeatherResponse>) {
        clearTable()
        list.forEach {
            cv.apply {
                clear()
                put(DBContract.WeatherEntry.TIME, it.dt)
                put(DBContract.WeatherEntry.TEMPERATURE, it.main.temp)
                put(DBContract.WeatherEntry.TEMPERATURE_MAX, it.main.temp_max)
                put(DBContract.WeatherEntry.TEMPERATURE_MIN, it.main.temp_min)
                put(DBContract.WeatherEntry.PRESSURE, it.main.pressure)
                put(DBContract.WeatherEntry.HUMIDITY, it.main.humidity)
                put(DBContract.WeatherEntry.WIND_SPEED, it.wind.speed)
                put(DBContract.WeatherEntry.WIND_DEGREE, it.wind.deg)
                put(DBContract.WeatherEntry.WEATHER_MAIN_CONDITION, it.weather[0].main)
                put(DBContract.WeatherEntry.DESCRIPTION, it.weather[0].description)
                put(DBContract.WeatherEntry.ICON, it.weather[0].icon)
                db.insert(DBContract.WeatherEntry.WEATHER_TABLE, null, cv)
            }
        }
    }

    private fun loadDataFromDB() {
        val list = mutableListOf<WeatherResponse>()
        val sqlQuery = SELECT_ALL_FROM_WEATHER_TABLE
        val cursor = db.rawQuery(sqlQuery, null)

        with(cursor) {
            while (moveToNext()) {
                val time =
                    getColumnIndexOrThrow(DBContract.WeatherEntry.TIME)
                val description =
                    getColumnIndexOrThrow(DBContract.WeatherEntry.DESCRIPTION)
                val main =
                    getColumnIndexOrThrow(DBContract.WeatherEntry.WEATHER_MAIN_CONDITION)
                val icon =
                    getColumnIndexOrThrow(DBContract.WeatherEntry.ICON)
                val temp =
                    getColumnIndexOrThrow(DBContract.WeatherEntry.TEMPERATURE)
                val tempMin =
                    getColumnIndexOrThrow(DBContract.WeatherEntry.TEMPERATURE_MIN)
                val tempMax =
                    getColumnIndexOrThrow(DBContract.WeatherEntry.TEMPERATURE_MAX)
                val pressure =
                    getColumnIndexOrThrow(DBContract.WeatherEntry.PRESSURE)
                val humidity =
                    getColumnIndexOrThrow(DBContract.WeatherEntry.HUMIDITY)
                val windSpeed =
                    getColumnIndexOrThrow(DBContract.WeatherEntry.WIND_SPEED)
                val windDegree =
                    getColumnIndexOrThrow(DBContract.WeatherEntry.WIND_DEGREE)
                list.add(
                    WeatherResponse(
                        getLong(time),
                        Main(
                            getFloat(temp),
                            getFloat(pressure),
                            getFloat(humidity),
                            getFloat(tempMin),
                            getFloat(tempMax)
                        ),
                        listOf(
                            Weather(
                                getString(main),
                                getString(description),
                                getString(icon)
                            )
                        ),
                        Wind(getFloat(windSpeed), getFloat(windDegree))
                    )
                )
            }
        }
        forecastList.value = list
        cursor.close()
    }

    companion object {
        const val UNITS = "selected_unit"
    }
}