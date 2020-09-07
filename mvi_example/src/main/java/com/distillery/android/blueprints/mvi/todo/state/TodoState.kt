package com.distillery.android.blueprints.mvi.todo.state

sealed class TodoState<out T> {
    object LoadingState : TodoState<Nothing>()
    data class DataState<out T>(val todoListFlow: T) : TodoState<T>()
    data class ErrorState(val errorMsg: Throwable?) : TodoState<Nothing>()
    data class ConfirmationState(val confirmationCode: ConfirmationCode) : TodoState<Nothing>()
}

sealed class ConfirmationCode {
    object SAVED : ConfirmationCode()
    object DELETED : ConfirmationCode()
    object UPDATED : ConfirmationCode()
}
