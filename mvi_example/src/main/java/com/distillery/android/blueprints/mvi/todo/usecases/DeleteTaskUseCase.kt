package com.distillery.android.blueprints.mvi.todo.usecases

import com.distillery.android.blueprints.mvi.todo.TodoListModel
import com.distillery.android.blueprints.mvi.todo.fetchUpdate
import com.distillery.android.blueprints.mvi.todo.state.ConfirmationCode
import com.distillery.android.blueprints.mvi.todo.state.TodoState
import com.distillery.android.domain.ToDoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.net.ConnectException

class DeleteTaskUseCase : KoinComponent {
    val toDoRepository: ToDoRepository by inject()

    suspend fun deleteTasks(idUnique: Long): Flow<TodoState<TodoListModel>> {
        return flow {
            try {
                toDoRepository.apply {
                    deleteToDo(idUnique)
                    emit(TodoState.DataState(fetchUpdate()))
                }
                emit(TodoState.ConfirmationState(ConfirmationCode.DELETED))
            } catch (connectException: ConnectException) {
                emit(TodoState.ErrorState(connectException))
            } catch (noSuchElement: NoSuchElementException) {
                emit(TodoState.ErrorState(noSuchElement))
            }
        }
    }
}