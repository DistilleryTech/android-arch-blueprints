package com.distillery.android.blueprints.mvi.todo

import com.distillery.android.blueprints.mvi.MviIntent

sealed class AddTodoIntent : MviIntent {
    class SaveTodo(val title: String, val description: String) : AddTodoIntent()
}
