package main.presenter

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
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

@Suppress("VariableNaming")
class Presenter(
    private val todoPendingListAdapter: TodoListAdapter,
    private val todoDoneListAdapter: TodoListAdapter,
    private val lifecycle: Lifecycle
): LifecycleObserver, KoinComponent, CoroutineScope {

    private val TAG = "TodoPresenter"
    private val repository: ToDoRepository by inject()
    var todoListAlltypes : List<ToDoModel> by Delegates.observable(listOf()){ property, oldValue, newValue ->

        todoPendingListAdapter.submitList(
            newValue.filter { item -> item.completedAt == null }
        )
        todoDoneListAdapter.submitList(
            newValue.filter { item -> item.completedAt != null }
        )
    }

    private val job = Job()
    private val coroutineExceptionHandler = CoroutineExceptionHandler { context,  throwable ->
        launch(Dispatchers.Main + Job()) {
            Log.d(TAG, "Error: ${throwable.message!!}")
            throwable.printStackTrace()
            // view.showError(throwable.message!!)
        }
    }
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO + coroutineExceptionHandler

    @InternalCoroutinesApi
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun start(){
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            startFlow()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        job.cancel()
    }

    @InternalCoroutinesApi
    @Suppress("TooGenericExceptionCaught")
    fun onCreateTodoClicked(){
        launch {
            repository.addToDo("Title", "Description")
        }
    }

    @InternalCoroutinesApi
    fun onClickCheckBox(item: ToDoModel, newState: Boolean){
        Log.d(TAG, "item[$item], newState[$newState]")
        launch {
            if(newState) {
                repository.completeToDo(item.uniqueId)
            } else {
                repository.deleteToDo(item.uniqueId)
            }
            startFlow()
        }
    }

    @InternalCoroutinesApi
    @Suppress("TooGenericExceptionCaught", "LongMethod")
    fun startFlow(){
        launch {
            repository.fetchToDos()
                .catch {
                    withContext(Dispatchers.Main) {
                        when(this@catch){
                            is IllegalArgumentException -> {
                                Log.d(TAG, "Cheating death!")
                            }
                            else -> {
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
}
