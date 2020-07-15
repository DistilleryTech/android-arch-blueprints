package com.distillery.android.blueprints.mvvm.todo.utils

import androidx.lifecycle.MutableLiveData

object Trigger

fun MutableLiveData<Trigger>.trigger() {
    value = Trigger
}
