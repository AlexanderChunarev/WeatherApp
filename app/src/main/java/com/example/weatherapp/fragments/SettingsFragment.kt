package com.example.weatherapp.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.weatherapp.R
import com.example.weatherapp.viewmodels.SharedViewModel
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {
    private lateinit var model: SharedViewModel
    private var unit = ""

    interface UnitCallBack {
        fun setUnit(s: String?)
    }

    var someEventListener: UnitCallBack? = null

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
                METRIC -> metric_radioButton.isChecked = true
                IMPERIAL -> imperial_radioButton.isChecked = true
            }
            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.metric_radioButton -> unit = METRIC
                    R.id.imperial_radioButton -> unit = IMPERIAL
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        someEventListener = try {
            activity as UnitCallBack
        } catch (e: ClassCastException) {
            throw ClassCastException(e.message)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        someEventListener?.setUnit(unit)
    }

    companion object {
        fun newInstance(unit: String) = SettingsFragment().apply {
            arguments = Bundle().apply {
                putString(RESPONSE_KEY, unit)
            }
        }

        private const val RESPONSE_KEY = "response_key"
        internal const val METRIC = "metric"
        internal const val IMPERIAL = "imperial"
    }
}
