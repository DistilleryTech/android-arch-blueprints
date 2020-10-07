package main.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.distillery.android.blueprints.mvp.todo.contract.TodoContract
import com.distillery.android.domain.ToDoRepository
import com.distillery.android.domain.models.ToDoModel
import com.distillery.android.mvp_example.R
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

private const val TAG = "TodoPresenter"

// TODO cover with tests
class PresenterImplementation(
    // TODO make view implement it and add method to the interface
    private val lifecycleOwner: LifecycleOwner,
    private val view: TodoContract.View
) : LifecycleObserver, KoinComponent, CoroutineScope,
    TodoContract.Presenter {
    private val repository: ToDoRepository by inject()
    private val errorHandler: CoroutineExceptionHandler by inject()
    // TODO use usual set logic, this kind of handling is not obvious and error prone
    private var todoListAllTypes: List<ToDoModel> by Delegates.observable(listOf()) { _, _, newValue ->
        view.showPendingTasks(
            newValue.filter { item -> item.completedAt == null }
        )

        view.showDoneTasks(
            newValue.filter { item -> item.completedAt != null }
        )
    }

    private val job = Job()
    private val coroutineScope: CoroutineScope by inject()
    // TODO (D.Balasundaram, 07.09.2020): I would recommend DI for the coroutine context,
    //  it's easier to test in that way
    override val coroutineContext: CoroutineContext =
        job + Dispatchers.IO + errorHandler

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
        coroutineScope.launch(errorHandler) {
            repository.fetchToDos()
                .collect {
                    // TODO move happy handling to another place, keep same level of abstraction
                    //  Q (A.Rudometkin, 30.09.2020): Does it really matter? Seems like we simply apply the new list
                    withContext(Dispatchers.Main) {
                        todoListAllTypes = it
                    }
                }
        }
    }

    override fun onClickCheckboxCompletion(item: ToDoModel) {
        // TODO display operation is progress or smth
        coroutineScope.launch(errorHandler) {
            repository.completeToDo(item.uniqueId)
        }
    }

    @InternalCoroutinesApi
    override fun onClickDeleteTask(item: ToDoModel) {
        // TODO display operation is progress or smth
        coroutineScope.launch(errorHandler) {
            repository.deleteToDo(item.uniqueId)
        }
    }

    @InternalCoroutinesApi
    override fun onClickAddTask() {
        // TODO display operation is progress or smth
        coroutineScope.launch(errorHandler) {
            repository.addToDo("Title", "Description")
        }
    }
}
