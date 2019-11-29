package com.example.weatherapp.fragments.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.responses.Response

abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(response: Response)
}