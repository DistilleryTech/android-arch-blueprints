package com.distillery.android.blueprints.mvvm.todo.viewmodel.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class LiveDataTest<T>(
        private val sourceLiveData: LiveData<T>,
        onChanged: ((T) -> Unit)? = null
) {
    private var result: T? = null

    private val observer = Observer<T> { t ->
        result = t
        onChanged?.invoke(t)
    }

    init {
        sourceLiveData.observeForever(observer)
    }

    fun removeObserver() {
        sourceLiveData.removeObserver(observer)
    }
}
