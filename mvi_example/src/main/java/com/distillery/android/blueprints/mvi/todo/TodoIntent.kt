package com.distillery.android.blueprints.mvi.todo

import com.distillery.android.blueprints.mvi.MviIntent

sealed class TodoIntent : MviIntent {
    object PopulateTodoList : TodoIntent()
    class DeleteTodo(val id: Long) : TodoIntent()
    class SaveTodo(val title: String, val description: String) : TodoIntent()
    class CompleteTodo(val id: Long) : TodoIntent()
}
