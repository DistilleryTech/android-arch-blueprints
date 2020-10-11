package com.distillery.android.blueprints.mvp.di

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.distillery.android.blueprints.mvp.todo.contract.TodoContract
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import com.distillery.android.blueprints.mvp.todo.contract.TodoPresenter
import org.koin.core.qualifier.named
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val mvpModule = module {
    factory(named(TAG)) {
        val job = Job()
        val errorHandler = get<CoroutineExceptionHandler>()
        CoroutineScope(Dispatchers.IO + job + errorHandler)
    }
    single<TodoContract.Presenter> { (view: TodoContract.View) ->
        TodoPresenter(view)
    }
    single {
        CoroutineExceptionHandler { _, throwable ->
            Log.d(TAG, "Error: ${throwable.message!!}")
        }
    }
}

private const val TAG = "MVP" // for named scope to avoid conflicts between modules
