package com.distillery.android.blueprints.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ExampleViewModel : ViewModel() {
    companion object {
        const val DELAY_MILLIS = 1000L
    }

    private val data: MutableLiveData<List<String>> by lazy { MutableLiveData<List<String>>() }

    init {
        loadData()
    }

    fun getData(): LiveData<List<String>> {
        return data
    }

    private fun loadData() {
        // Do an asynchronous operation to fetch data.
        viewModelScope.launch {
            // TODO: Get your data from a repository or directly from your API, database, etc
            delay(DELAY_MILLIS)
            data.value = listOf("new values")
        }
    }
}
