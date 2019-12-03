package com.example.weatherapp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import com.example.weatherapp.R
import com.example.weatherapp.viewmodels.SharedViewModel
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {
    private lateinit var model: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        }!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            when (arguments!!.getString(RESPONSE_KEY)) {
                "metric" -> metric_radioButton.isChecked = true
                "imperial" -> imperial_radioButton.isChecked = true
            }
        }
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.metric_radioButton -> model.selectedUnit("metric")
                R.id.imperial_radioButton -> model.selectedUnit("imperial")
            }
        }
    }

    companion object {
        fun newInstance(unit: String) = SettingsFragment().apply {
            arguments = Bundle().apply {
                putString(RESPONSE_KEY, unit)
            }
        }

        const val UNIT_KEY = "selected_unit"
        private const val RESPONSE_KEY = "response_key"
    }
}
