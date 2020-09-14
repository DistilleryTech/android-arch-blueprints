package com.distillery.android.blueprints.mvvm.todo.utils

import com.distillery.android.blueprints.mvvm.todo.viewmodel.TodoListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val mvvmModule = module {
    viewModel { TodoListViewModel(get(), AppErrorHandler()) }
}
