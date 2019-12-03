package com.example.weatherapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val selected = MutableLiveData<String>()

    fun selectedUnit(item: String) {
        selected.value = item
    }
}