package com.distillery.android.ui

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.widget.TextView

fun TextView.strikeThrough() {
    paintFlags = paintFlags or STRIKE_THRU_TEXT_FLAG
}
