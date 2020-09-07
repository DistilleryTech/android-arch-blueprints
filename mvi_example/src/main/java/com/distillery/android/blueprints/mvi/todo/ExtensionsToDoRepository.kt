package com.distillery.android.blueprints.mvi.todo

import com.distillery.android.domain.ToDoRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

suspend fun ToDoRepository.fetchUpdate() = fetchToDos()
        .map {
            val partition = it.partition { model -> model.completedAt == null }
            TodoListModel(partition.first, partition.second)
        }
        .first()
