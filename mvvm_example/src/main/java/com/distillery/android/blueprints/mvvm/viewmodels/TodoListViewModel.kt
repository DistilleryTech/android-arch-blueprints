package com.distillery.android.blueprints.mvvm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.distillery.android.blueprints.mvvm.managers.AppErrorHandler
import com.distillery.android.blueprints.mvvm.managers.EventType
import com.distillery.android.blueprints.mvvm.managers.SingleLiveEvent
import com.distillery.android.domain.ToDoRepository
import com.distillery.android.domain.models.ToDoModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TodoListViewModel(
    private val toDoRepository: ToDoRepository,
    private val errorHandler: AppErrorHandler
) : ViewModel() {

    // collects the flow and convert it to livedata with view model scope
    private val allTodoListLiveData: LiveData<List<ToDoModel>> =
            toDoRepository.fetchToDos()
                    .catch { _closeActivityLiveData.value = true }
                    .asLiveData(viewModelScope.coroutineContext)

    val todoListLiveData = allTodoListLiveData.map {
        it.filter { item -> item.completedAt == null } // non-completedTodo list
    }

    val completedTodoListLiveData = allTodoListLiveData.map {
        it.filter { item -> item.completedAt != null } // completedTodo list
    }

    private val _snackBarMessageLiveData = SingleLiveEvent<EventType>()
    val snackBarMessageLiveData: LiveData<EventType> = _snackBarMessageLiveData

    private val _closeActivityLiveData = SingleLiveEvent<Boolean>()
    val closeActivityLiveData: LiveData<Boolean> = _closeActivityLiveData

    /**
     * calls the repo to make the item completed
     */
    fun completeTodo(toDoModel: ToDoModel) {
        _snackBarMessageLiveData.value = errorHandler {
            toDoRepository.completeToDo(toDoModel.uniqueId)
        } ?: EventType.COMPLETE
    }

    /**
     * launch coroutine and calls the repo to make the item deleted
     */
    fun deleteTodo(toDoModel: ToDoModel) {
        viewModelScope.launch {
            _snackBarMessageLiveData.value = errorHandler {
                toDoRepository.deleteToDo(toDoModel.uniqueId)
            } ?: EventType.DELETE
        }
    }

    /**
     * launch coroutine and calls the repo to make the item added
     */
    fun addTodo(title: String, description: String) {
        viewModelScope.launch {
            _snackBarMessageLiveData.value = errorHandler {
                toDoRepository.addToDo(title, description)
            } ?: EventType.ADD
        }
    }
}
