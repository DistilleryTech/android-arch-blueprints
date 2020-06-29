package main.presenter

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.distillery.android.domain.FakeToDoRepository
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

@Suppress("VariableNaming")
class Presenter(
    private val todoListAdapter: TodoListAdapter,
    private val lifecycle: Lifecycle
): LifecycleObserver, KoinComponent, CoroutineScope {

    private val TAG = "TodoPresenter"
    private val repository: FakeToDoRepository by inject()

    private val job = Job()
    private val coroutineExceptionHandler = CoroutineExceptionHandler { context,  throwable ->
        launch(Dispatchers.Main + Job()) {
            Log.d(TAG, "Error: ${throwable.message!!}")
            throwable.printStackTrace()
            // view.showError(throwable.message!!)
        }
    }
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO + coroutineExceptionHandler

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun start(){
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            Log.d(TAG,"RESUME")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        // disconnect if connected
        Log.d(TAG,"STOP")
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
    @Suppress("TooGenericExceptionCaught" ,"LongMethod")
    fun startFlow(){
        launch {
            repository.fetchToDos()
                .catch {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "ErrorCatch: $this@catch")
                    }
                }
                .collect {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "listSize = ${it.size}")
                        todoListAdapter.submitList(it)
                    }
                }
        }
    }
}
