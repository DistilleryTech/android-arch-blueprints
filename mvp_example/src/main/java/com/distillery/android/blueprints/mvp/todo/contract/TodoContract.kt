package com.distillery.android.blueprints.mvp.todo.contract

import com.distillery.android.domain.models.ToDoModel

interface TodoContract {
    interface View {
        fun showError(message: Int)
        fun notifyTaskDeleted()
        fun showPendingTasks(tasks: List<ToDoModel>)
        fun showDoneTasks(tasks: List<ToDoModel>)
    }
    interface Presenter {
        fun onClickAddTask()
        fun onClickCheckboxCompletion(item: ToDoModel)
        fun onClickDeleteTask(item: ToDoModel)
    }
}
