package com.example.weatherapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.weatherapp.R
import com.example.weatherapp.responses.WeatherResponse
import kotlinx.android.synthetic.main.fragment_info.view.*

class InfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_info, container, false)
        if (arguments != null) {
            rootView.apply {
                val weatherResponse = arguments!!.getSerializable(RESPONSE_KEY) as WeatherResponse
                humidity_value_text.text = weatherResponse.main.humidity
                pressure_value_text.text = weatherResponse.main.pressure
            }
        }
        return rootView
    }

    companion object {
        fun newInstance(response: WeatherResponse) = InfoFragment().apply {
            arguments = Bundle().apply {
                putSerializable(RESPONSE_KEY, response)
            }
        }

        private const val RESPONSE_KEY = "response_key"
    }
}
