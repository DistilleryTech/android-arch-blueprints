package main.presenter

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.distillery.android.blueprints.mvp.todo.contract.TodoContract
import com.distillery.android.domain.ToDoRepository
import com.distillery.android.domain.models.ToDoModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import main.view.TodoListAdapter
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

private val TAG = "TodoPresenter"

class PresenterImplementation(
    private val lifecycleOwner: LifecycleOwner,
    private val view: TodoContract.View
) : LifecycleObserver, KoinComponent, CoroutineScope,
    TodoContract.Presenter{
    private val repository: ToDoRepository by inject()
    var todoListAlltypes: List<ToDoModel> by Delegates.observable(listOf()) { _, _, newValue ->

        view.showPendingTasks(
            newValue.filter { item -> item.completedAt ==  null }
        )

        view.showDoneTasks(
            newValue.filter { item -> item.completedAt != null }
        )
    }

    private val job = Job()
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        launch(Dispatchers.Main + Job()) {
            Log.d(TAG, "Error: ${throwable.message!!}")
            throwable.printStackTrace()
            view.showError(throwable.message ?: "Undefined error")
        }
    }
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO + coroutineExceptionHandler

    @InternalCoroutinesApi
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun start() {
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            startFlow()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        job.cancel()
    }

    @InternalCoroutinesApi
    fun startFlow() {
        launch {
            repository.fetchToDos()
                .catch {
                    withContext(Dispatchers.Main) {
                        when (this@catch) {
                            is IllegalArgumentException -> {
                                view.showError("Cheating death!")
                                Log.d(TAG, "Cheating death!")
                            }
                            else -> {
                                view.showError("Unknown exception")
                                Log.d(TAG, "Unknown exception")
                            }
                        }
                    }
                }
                .collect {
                    withContext(Dispatchers.Main) {
                        todoListAlltypes = it
                    }
                }
        }
    }

    override fun onClickCheckboxCompletion(item: ToDoModel) {
        repository.completeToDo(item.uniqueId)
    }

    @InternalCoroutinesApi
    override fun onClickDeleteTask(item: ToDoModel) {
        launch {
            repository.deleteToDo(item.uniqueId)
        }
    }

    @InternalCoroutinesApi
    override fun onClickAddTask() {
        launch {
            repository.addToDo("Title", "Description")
        }
    }
}
