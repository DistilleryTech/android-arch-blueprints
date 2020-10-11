package com.distillery.android.blueprints.mvp.todo.contract

import androidx.lifecycle.lifecycleScope
import com.distillery.android.domain.ToDoRepository
import com.distillery.android.domain.models.ToDoModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.net.ConnectException
import java.util.Date

class TodoPresenter(
    private val view: TodoContract.View
) : TodoContract.Presenter, KoinComponent {

    private val repository: ToDoRepository by inject()

    private val scope = view.lifecycleScope

    private var relevantData: List<ToDoModel> = emptyList()

    init {
        scope.launchWhenStarted {
            view.displayProgress()
            try {
                // TODO think about moving context switch to somewhere else. aageychenko 11 Oct 20
                //   cause that looks kinda ugly
                withContext(IO) {
                    repository.fetchToDos()
                            .collect {
                                withContext(Main) {
                                    handleData(it)
                                }
                            }
                }
            } catch (exception: IllegalArgumentException) {
                handleError()
            }
        }
    }

    private fun handleError() {
        view.hideProgress()
        view.showLoadingError()
    }

    private fun handleData(list: List<ToDoModel>) {
        relevantData = list
        view.hideProgress()
        view.displayData(relevantData)
    }

    private fun TodoContract.View.displayData(list: List<ToDoModel>) {
        showPendingTasks(list.filter { item -> !item.isCompleted })
        showDoneTasks(list.filter { item -> item.isCompleted })
    }

    override fun onClickCheckboxCompletion(item: ToDoModel) {
        scope.launchWhenStarted {
            try {
                withContext(IO) {
                    repository.completeToDo(item.uniqueId)
                }
            } catch (error: UnsupportedOperationException) {
                view.showCompletingError()
                view.displayData(relevantData)
            }
        }
    }

    override fun onClickDeleteTask(item: ToDoModel) {
        scope.launchWhenStarted {
            view.displayProgress()
            withContext(IO) {
                repository.deleteToDo(item.uniqueId)
            }
            view.hideProgress()
        }
    }

    override fun onClickAddTask() {
        scope.launchWhenStarted {
            try {
                view.displayProgress()
                withContext(IO) {
                    repository.addToDo("Title", "Description ${Date().toGMTString()}")
                }
            } catch (error: ConnectException) {
                view.showAddingItemError()
                view.displayData(relevantData)
            } finally {
                view.hideProgress()
            }
        }
    }
}
