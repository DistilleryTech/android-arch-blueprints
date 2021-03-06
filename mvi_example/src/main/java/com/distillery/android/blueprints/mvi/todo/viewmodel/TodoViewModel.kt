package com.distillery.android.blueprints.mvi.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.distillery.android.blueprints.mvi.MviViewModel
import com.distillery.android.blueprints.mvi.emitState
import com.distillery.android.blueprints.mvi.todo.TodoIntent
import com.distillery.android.blueprints.mvi.todo.TodoListModel
import com.distillery.android.blueprints.mvi.todo.state.TodoState
import com.distillery.android.blueprints.mvi.todo.usecases.CompleteTaskUseCase
import com.distillery.android.blueprints.mvi.todo.usecases.DeleteTaskUseCase
import com.distillery.android.blueprints.mvi.todo.usecases.GetToDoListUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

@ExperimentalCoroutinesApi
class TodoViewModel : ViewModel(), MviViewModel<TodoIntent>, KoinComponent {

    private val getTodoListUseCase: GetToDoListUseCase by inject()
    private val deleteTaskUseCase: DeleteTaskUseCase by inject()
    private val completeTaskUseCase: CompleteTaskUseCase by inject()

    private val mutableState = MutableStateFlow<TodoState<TodoListModel>>(TodoState.LoadingState)
    val todoState: StateFlow<TodoState<TodoListModel>>
        get() = mutableState

    override fun proccessIntents(intents: Flow<TodoIntent>) {
        viewModelScope.launch {
            intents.collect { intent ->
                when (intent) {
                    is TodoIntent.PopulateTodoList -> {
                        getTodoList()
                    }
                    is TodoIntent.DeleteTodo -> {
                        deleteTodo(intent.id)
                    }
                    is TodoIntent.CompleteTodo -> {
                        completeTodo(intent.id)
                    }
                }
            }
        }
    }

    private fun getTodoList() {
        viewModelScope.launch {
            getTodoListUseCase
                    .getToDoList()
                    .emitState(mutableState)
        }
    }

    private fun deleteTodo(id: Long) {
        viewModelScope.launch {
            emitLoadingState()
            deleteTaskUseCase
                    .deleteTasks(id)
                    .emitState(mutableState)
        }
    }

    private fun completeTodo(id: Long) {
        viewModelScope.launch {
            emitLoadingState()
            completeTaskUseCase
                    .completeTasks(id)
                    .emitState(mutableState)
        }
    }

    private fun emitLoadingState() {
        mutableState.value = TodoState.LoadingState
    }
}
