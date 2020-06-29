package com.distillery.android.blueprints.mvi

import com.distillery.android.blueprints.mvi.todo.usecases.CompleteTaskUseCase
import com.distillery.android.blueprints.mvi.todo.usecases.DeleteTaskUseCase
import com.distillery.android.blueprints.mvi.todo.usecases.GetToDoListUseCase
import com.distillery.android.blueprints.mvi.todo.usecases.SaveTaskUseCase
import com.distillery.android.blueprints.mvi.todo.viewmodel.TodoViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val mviModule = module {
    // module declarations
    viewModel { TodoViewModel() }
    single { GetToDoListUseCase() }
    single { SaveTaskUseCase() }
    single { DeleteTaskUseCase() }
    single { CompleteTaskUseCase() }
}
