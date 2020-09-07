package com.distillery.android.blueprints.mvi

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
interface MviViewModel<I : MviIntent> {
    fun proccessIntents(intents: Flow<I>)
}
