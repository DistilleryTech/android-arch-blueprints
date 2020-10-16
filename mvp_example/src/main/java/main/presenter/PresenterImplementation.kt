package main.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.distillery.android.blueprints.mvp.todo.contract.TodoContract
import com.distillery.android.domain.ToDoRepository
import com.distillery.android.domain.models.ToDoModel
import com.distillery.android.mvp_example.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.properties.Delegates

private val TAG = "TodoPresenter"

class PresenterImplementation(
    private val lifecycleOwner: LifecycleOwner,
    private val view: TodoContract.View
) : LifecycleObserver, KoinComponent,
    TodoContract.Presenter {
    private val repository: ToDoRepository by inject()
    private val job: Job by inject()
    val coroutineScope: CoroutineScope by inject()

    var todoListAlltypes: List<ToDoModel> by Delegates.observable(listOf()) { _, _, newValue ->

        view.showPendingTasks(
            newValue.filter { item -> item.completedAt ==  null }
        )

        view.showDoneTasks(
            newValue.filter { item -> item.completedAt != null }
        )
    }

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
        coroutineScope.launch {
            repository.fetchToDos()
                .catch {
                    withContext(Dispatchers.Main) {
                        when (this@catch) {
                            is IllegalArgumentException ->
                                view.showError(R.string.error_cheating)
                            else ->
                                view.showError(R.string.error_undefined)
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
        coroutineScope.launch {
            repository.deleteToDo(item.uniqueId)
        }
    }

    @InternalCoroutinesApi
    override fun onClickAddTask() {
        coroutineScope.launch {
            repository.addToDo("Title", "Description")
        }
    }
}
