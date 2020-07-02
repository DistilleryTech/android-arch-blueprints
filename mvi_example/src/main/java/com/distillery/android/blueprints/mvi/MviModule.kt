package com.distillery.android.blueprints.mvi

import com.distillery.android.blueprints.mvi.todo.usecases.CompleteTaskUseCase
import com.distillery.android.blueprints.mvi.todo.usecases.DeleteTaskUseCase
import com.distillery.android.blueprints.mvi.todo.usecases.GetToDoListUseCase
import com.distillery.android.blueprints.mvi.todo.usecases.SaveTaskUseCase
import com.distillery.android.blueprints.mvi.todo.viewmodel.AddTodoViewModel
import com.distillery.android.blueprints.mvi.todo.viewmodel.TodoViewModel
import com.distillery.android.domain.FakeToDoRepository
import com.distillery.android.domain.ToDoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val mviModule = module {
    // module declarations
    factory { CoroutineScope(Dispatchers.IO + SupervisorJob()) }
    single<ToDoRepository> { FakeToDoRepository(get()) }
    viewModel { TodoViewModel() }
    viewModel { AddTodoViewModel() }
    single { (scope: CoroutineScope) -> FakeToDoRepository(scope) }
    single { GetToDoListUseCase() }
    single { SaveTaskUseCase() }
    single { DeleteTaskUseCase() }
    single { CompleteTaskUseCase() }
}
