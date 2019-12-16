package com.example.thirdhomework.listener

import java.io.Serializable

interface OnItemListener : Serializable {
    fun onClickItem(position: Int)
}