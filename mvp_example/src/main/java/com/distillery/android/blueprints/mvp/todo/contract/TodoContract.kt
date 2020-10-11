package com.distillery.android.blueprints.mvp.todo.contract

import androidx.lifecycle.LifecycleOwner
import com.distillery.android.domain.models.ToDoModel
import com.distillery.android.ui.progress.WithProgressIndicator

interface TodoContract {
    interface View : LifecycleOwner, WithProgressIndicator {
        fun showLoadingError()
        fun showCompletingError()
        fun showAddingItemError()
        fun showPendingTasks(tasks: List<ToDoModel>)
        fun showDoneTasks(tasks: List<ToDoModel>)
    }

    interface Presenter {
        fun onClickAddTask()
        fun onClickCheckboxCompletion(item: ToDoModel)
        fun onClickDeleteTask(item: ToDoModel)
    }
}
