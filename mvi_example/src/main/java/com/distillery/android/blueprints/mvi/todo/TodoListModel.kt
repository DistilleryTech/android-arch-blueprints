package com.distillery.android.blueprints.mvi.todo

import com.distillery.android.domain.models.ToDoModel

data class TodoListModel(val todoList: List<ToDoModel>, val completedTodoList: List<ToDoModel>)
