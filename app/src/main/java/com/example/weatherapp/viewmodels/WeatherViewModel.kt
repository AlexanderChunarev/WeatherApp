package com.example.weatherapp.viewmodels

import android.content.ContentValues
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.*
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.weatherapp.cache.SPCache
import com.example.weatherapp.data.DBHelper
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
    private val cv = ContentValues()

    init {
        loadWeatherData()
    }

    private fun createInputData(): Data {
        return Data.Builder()
            .putString(UNITS, spCache.unit)
            .build()
    }

    fun loadWeatherData() {
        if (checkConnection()) {
            val request = OneTimeWorkRequest.Builder(WeatherWorker::class.java)
                .setInputData(createInputData())
                .build()
            WorkManager.getInstance(context).enqueue(request)
            WorkManager.getInstance(context).getWorkInfoByIdLiveData(request.id)
                .observe(lifecycleOwner, Observer {
                    if (it.state.isFinished) {
                        WeatherWorker.apply {
                            currentWeatherForecast.value = fetchCurrentForecast()
                            forecastList.value = fetchForecast()
                            deleteDB()
                            this.fetchForecast()?.let { it1 -> writeToDB(it1) }
                            loadDataFromDB()
                        }
                    }
                })
        } else {
            loadDataFromDB()
        }

    }

    private fun checkConnection(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetwork != null
    }


    private fun deleteDB() {
        context.apply {
            deleteDatabase(DBHelper.DATABASE_NAME)
        }
    }

    private fun writeToDB(list: List<WeatherResponse>) {
        list.forEach {
            cv.apply {
                clear()
                put(DBHelper.TIME_KEY, it.dt)
                db.insert(DBHelper.WEATHER_TABLE, null, cv)

                clear()
                put(DBHelper.TEMPERATURE, it.main.temp)
                put(DBHelper.TEMPERATURE_MAX, it.main.temp_max)
                put(DBHelper.TEMPERATURE_MIN, it.main.temp_min)
                put(DBHelper.PRESSURE, it.main.pressure)
                put(DBHelper.HUMIDITY, it.main.humidity)
                db.insert(DBHelper.MAIN_INFO_TABLE, null, cv)
            }
        }
    }

    private fun loadDataFromDB() {
        val mutable = mutableListOf<WeatherResponse>()
        val sqlQuery =
            "select * from ${DBHelper.WEATHER_TABLE} as w inner join ${DBHelper.MAIN_INFO_TABLE} as m where w._id = m._id"
        val curs = db.rawQuery(sqlQuery, null)

        if (curs.moveToFirst()) {
            do {
                val timeIndex = curs.getColumnIndex(DBHelper.TIME_KEY)
                //val nameIndex = curs.getColumnIndex(DBHelper.NAME_KEY)
                val tempIndex = curs.getColumnIndex(DBHelper.TEMPERATURE)
                val pressureIndex = curs.getColumnIndex(DBHelper.PRESSURE)
                val humIndex = curs.getColumnIndex(DBHelper.HUMIDITY)
                val weather = Weather("main", "desc", "icon")

                mutable.add(
                    WeatherResponse(
                        curs.getLong(timeIndex),
                        Main(
                            curs.getFloat(tempIndex),
                            curs.getString(pressureIndex),
                            curs.getString(humIndex),
                            0.0f,
                            0.0f
                        ),
                        listOf(weather),
                        Wind(0.0, 0.0)
                    )
                )
            } while (curs.moveToNext())
        } else {
            Log.d("mlog", "0 rows")
        }
        forecastList.value = mutable
        curs.close()
        dbHelper.close()
    }

    companion object {
        const val UNITS = "selected_unit"
    }
}