package com.distillery.android.blueprints.mvvm.managers

import androidx.lifecycle.MutableLiveData

object Trigger

fun MutableLiveData<Trigger>.trigger() {
    value = Trigger
}
