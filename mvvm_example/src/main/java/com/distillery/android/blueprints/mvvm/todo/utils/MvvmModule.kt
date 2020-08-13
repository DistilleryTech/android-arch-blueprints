package com.distillery.android.blueprints.mvvm.todo.utils

import com.distillery.android.blueprints.mvvm.todo.viewmodel.TodoListViewModel
import com.distillery.android.domain.FakeToDoRepository
import com.distillery.android.domain.ToDoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val mvvmModule = module {
    factory(named(TAG)) {
        val errorHandler = get<AppErrorHandler>()
        CoroutineScope(Dispatchers.IO + Job() + errorHandler.unCaughtExceptionHandler)
    }
    single<ToDoRepository>(named(TAG)) { FakeToDoRepository(get(named(TAG))) }
    single { AppErrorHandler() }
    viewModel { TodoListViewModel(get(named(TAG)), get()) }
}

private const val TAG = "MVVM" // for named scope to avoid conflicts between modules
