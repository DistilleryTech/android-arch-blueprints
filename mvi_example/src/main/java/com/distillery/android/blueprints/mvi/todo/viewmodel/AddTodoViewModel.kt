package com.distillery.android.blueprints.mvi.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.distillery.android.blueprints.mvi.MviViewModel
import com.distillery.android.blueprints.mvi.todo.AddTodoIntent
import com.distillery.android.blueprints.mvi.todo.TodoListModel
import com.distillery.android.blueprints.mvi.todo.state.TodoState
import com.distillery.android.blueprints.mvi.todo.usecases.SaveTaskUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

@ExperimentalCoroutinesApi
class AddTodoViewModel : ViewModel(), MviViewModel<AddTodoIntent>, KoinComponent {

    private val saveTaskUseCase: SaveTaskUseCase by inject()

    private val mutableState = MutableStateFlow<TodoState<TodoListModel>>(TodoState.LoadingState)
    val todoState: StateFlow<TodoState<TodoListModel>>
        get() = mutableState

    override fun proccessIntents(intents: Flow<AddTodoIntent>) {
        viewModelScope.launch {
            intents.collect { intent ->
                when (intent) {
                    is AddTodoIntent.SaveTodo -> {
                        saveTodo(intent.title, intent.description)
                    }
                }
            }
        }
    }

    private fun saveTodo(title: String, description: String) {
        if (title.isNotBlank() && description.isNotBlank()) {
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
        viewModelScope.launch {

        }
    }
}
