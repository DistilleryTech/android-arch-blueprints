package com.distillery.android.ui

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.widget.TextView

fun TextView.strikeThrough(show: Boolean) {
    paintFlags = if (show) {
        paintFlags or STRIKE_THRU_TEXT_FLAG
    } else {
        paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
    }
}
