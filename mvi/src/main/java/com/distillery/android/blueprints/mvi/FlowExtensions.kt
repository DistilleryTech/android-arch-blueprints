package com.distillery.android.blueprints.mvi

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
suspend fun <T> Flow<T>.emitState(mutableStateFlow: MutableStateFlow<T>) {
    collect { state ->
        mutableStateFlow.value = state
    }
}
