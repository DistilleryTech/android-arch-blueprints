package com.distillery.android.blueprints.mvi.todo.usecases

import com.distillery.android.blueprints.mvi.todo.TodoListModel
import com.distillery.android.blueprints.mvi.todo.state.TodoState
import com.distillery.android.domain.ToDoRepository
import kotlinx.coroutines.flow.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class GetToDoListUseCase : KoinComponent {
    private val toDoRepository: ToDoRepository by inject()

    /**
     * Fetches To Do items from the repository.
     * emits states with the data as the repository publish it.
     * or emits an error state.
     */
    suspend fun getToDoList(): Flow<TodoState<TodoListModel>> {
        return flow {
            emit(TodoState.LoadingState)
            toDoRepository.fetchToDos()
                    .catch { e -> emit(TodoState.ErrorState(e)) }
                    .map {
                        val pendingTodoList = it.filter { model -> model.completedAt == null }
                        val completedTodoList = it.filter { model -> model.completedAt != null }
                        TodoState.DataState(TodoListModel(pendingTodoList, completedTodoList))
                    }
                    .collect { emit(it) }
        }
    }
}
