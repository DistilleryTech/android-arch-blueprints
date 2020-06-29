package com.distillery.android.blueprints.mvi.todo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.distillery.android.blueprints.mvi.MviViewModel
import com.distillery.android.blueprints.mvi.todo.TodoIntent
import com.distillery.android.blueprints.mvi.todo.TodoListModel
import com.distillery.android.blueprints.mvi.todo.state.TodoState
import com.distillery.android.blueprints.mvi.todo.usecases.DeleteTaskUseCase
import com.distillery.android.blueprints.mvi.todo.usecases.GetToDoListUseCase
import com.distillery.android.blueprints.mvi.todo.usecases.SaveTaskUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

@ExperimentalCoroutinesApi
class TodoViewModel : ViewModel(), MviViewModel<TodoIntent>, KoinComponent {

    private val getTodoListUseCase: GetToDoListUseCase by inject()
    private val deleteTaskUseCase: DeleteTaskUseCase by inject()
    private val saveTaskUseCase: SaveTaskUseCase by inject()

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
                    is TodoIntent.SaveTodo -> {
                        saveTodo(intent.title, intent.description)
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getTodoList() {
        viewModelScope.launch {
            getTodoListUseCase.getToDoList()
                    .collect { state ->
                        when (state) {
                            is TodoState.DataState -> {
                                mutableState.value = state
                            }
                            is TodoState.ErrorState -> {
                                Log.e("ERROR ___>>>>", "${state.errorMsg?.toString()}")
                                mutableState.value = state
                            }
                        }
                    }
        }
    }

    private fun deleteTodo(id: Long) {
        viewModelScope.launch {
            mutableState.value = TodoState.LoadingState
            deleteTaskUseCase.deleteTasks(id)
                    .collect { state ->
                        when (state) {
                            is TodoState.DataState -> {
                                mutableState.value = state
                            }
                            is TodoState.ConfirmationState -> {
                                mutableState.value = state
                            }
                            is TodoState.ErrorState -> {
                                mutableState.value = state
                            }
                        }
                    }
        }
    }

    private fun saveTodo(title: String, description: String) {
        viewModelScope.launch {
            saveTaskUseCase.saveTask(title, description)
                    .collect { state ->
                        when (state) {
                            is TodoState.DataState -> {
                                mutableState.value = state
                            }
                            is TodoState.ConfirmationState -> {
                                mutableState.value = state
                            }
                            is TodoState.ErrorState -> {
                                mutableState.value = state
                            }
                        }
                    }
        }
    }
}
