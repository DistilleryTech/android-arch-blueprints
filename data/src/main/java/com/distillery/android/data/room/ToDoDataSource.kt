package com.distillery.android.data.room

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.distillery.android.data.room.dao.ToDoDao
import com.distillery.android.data.room.mapper.TodoDataToDomainMapper
import com.distillery.android.data.room.models.ToDoEntity
import com.distillery.android.domain.ToDoRepository
import com.distillery.android.domain.models.ToDoModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.net.ConnectException
import java.util.Date

@VisibleForTesting
const val DELAY_FOR_TODO_OPERATION = 3000L

/** Allow to do 5 actions before getting an error */
private const val RANDOM_NUMBER = 5
private var errorCounter = 1

class ToDoDataSource(
    private val dao: ToDoDao,
    private val mapper: TodoDataToDomainMapper
) : ToDoRepository {

    override val connectionStatus: LiveData<Boolean>
        get() = MutableLiveData<Boolean>().apply {
            postValue(true)
        }

    override fun fetchToDos(): Flow<List<ToDoModel>> {
        // region imitation of live-errors
        errorCounter++
        if (errorCounter % RANDOM_NUMBER == 0) {
            return flow {
                throw IllegalArgumentException("You died")
            }
        }
        // endregion
        return dao.fetchEverything().map { list -> list.map { model -> mapper.map(model) } }
    }

    override suspend fun addToDo(title: String, description: String) {
        // region imitation of live-errors
        delay(DELAY_FOR_TODO_OPERATION)
        errorCounter++
        if (errorCounter % RANDOM_NUMBER == 0) {
            throw ConnectException("You died")
        }
        // endregion
        dao.add(ToDoEntity(title = title, description = description, createdAt = Date().time))
    }

    override fun completeToDo(uniqueId: Long) {
        // region imitation of live-errors
        errorCounter++
        if (errorCounter % RANDOM_NUMBER == 0) {
            throw UnsupportedOperationException("You died")
        }
        // endregion
        dao.completeToDo(Date().time, uniqueId)
    }

    override suspend fun deleteToDo(uniqueId: Long) {
        delay(DELAY_FOR_TODO_OPERATION)
        dao.deleteById(uniqueId)
    }
}
