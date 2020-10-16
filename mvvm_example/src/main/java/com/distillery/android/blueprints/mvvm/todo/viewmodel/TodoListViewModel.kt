package com.distillery.android.blueprints.mvvm.todo.viewmodel

import androidx.lifecycle.*
import com.distillery.android.blueprints.mvvm.todo.utils.*
import com.distillery.android.domain.ToDoRepository
import com.distillery.android.domain.models.ToDoModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TodoListViewModel(
        private val toDoRepository: ToDoRepository,
        private val errorHandler: AppErrorHandler
) : ViewModel() {

    private val refreshLiveData = MutableLiveData<Trigger>()

    private val _title: MutableLiveData<String> = MutableLiveData("")
    val title: LiveData<String> = _title

    private val _description = MutableLiveData("")
    val description: LiveData<String> = _description

    private val allTodoListLiveData = refreshLiveData.switchMap {
        toDoRepository.fetchToDos()
                .catch { error: Throwable -> println(error) }
                .asLiveData(viewModelScope.coroutineContext)
    }

    val todoListLiveData = allTodoListLiveData.map {
        it.filter { item -> item.completedAt == null }
    }

    val completedTodoListLiveData = allTodoListLiveData.map {
        it.filter { item -> item.completedAt != null }
    }

    val totalTodoListLiveData: LiveData<ArrayList<ToDoModel?>> = allTodoListLiveData.map {
        val nonCompleted = it.filter { item -> item.completedAt == null }
        val completed = it.filter { item -> item.completedAt != null }
        val arrayList = ArrayList<ToDoModel?>()
        arrayList.addAll(nonCompleted)
        if (completed.isNotEmpty()) {
            arrayList.add(null)
            arrayList.addAll(completed)
        }
        arrayList
    }

    private val _snackBarMessageLiveData = SingleLiveEvent<EventType>()
    val snackBarMessageLiveData: LiveData<EventType> = _snackBarMessageLiveData

    private val connectionStatusLiveData = toDoRepository.connectionStatus.map { isAlive ->
        if (!isAlive) {
            _snackBarMessageLiveData.value = EventType.RECONNECTING
            refreshLiveData.trigger()
        }
        isAlive
    }

    private val connectionStatusObserver = Observer<Boolean> {}

    init {
        refreshLiveData.trigger()
        connectionStatusLiveData.observeForever(connectionStatusObserver)
    }

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

    fun addTodo() {
        viewModelScope.launch {
            _snackBarMessageLiveData.value = errorHandler {
                val todoTitle = _title.value ?: throw UnsupportedOperationException("title empty")
                val todoDescription =
                        _description.value ?: throw UnsupportedOperationException("Description empty")
                toDoRepository.addToDo(todoTitle, todoDescription)
            } ?: EventType.ADD
        }
    }

    override fun onCleared() {
        connectionStatusLiveData.removeObserver(connectionStatusObserver)
    }

    fun onAddTitleChange(updatedTitle: String) {
        _title.value = updatedTitle
    }

    fun onDescriptionChange(updatedTitle: String) {
        _description.value = updatedTitle
    }

}
