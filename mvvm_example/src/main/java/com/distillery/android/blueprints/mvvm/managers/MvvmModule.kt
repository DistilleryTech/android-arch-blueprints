package com.distillery.android.blueprints.mvvm.managers

import com.distillery.android.blueprints.mvvm.viewmodels.TodoListViewModel
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
    val tag = "MVVM" // for named scope to avoid conflicts between modules
    factory(named(tag)) {
        val errorHandler = get<AppErrorHandler>()
        CoroutineScope(Dispatchers.IO + Job() + errorHandler.unCaughtExceptionHandler)
    }
    single<ToDoRepository>(named(tag)) { FakeToDoRepository(get(named(tag))) }
    single { AppErrorHandler() }
    viewModel { TodoListViewModel(get(named(tag)), get()) }
}
