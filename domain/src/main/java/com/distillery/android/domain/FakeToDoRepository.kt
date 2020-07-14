package com.distillery.android.domain

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.distillery.android.domain.models.ToDoModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.util.Date
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * Static generator to guarantee uniqueness of ids across the app.
 */
private val idGenerator = AtomicLong(0)

@VisibleForTesting
const val DELAY_OF_VALUES_GENERATOR = 5000L

@VisibleForTesting
const val DELAY_FOR_TODO_OPERATION = 1500L

private const val TODOS_ITEMS_TO_THROW = 10

private const val TODO_ITEMS_CHANNEL_TO_THROW = 5

@ExperimentalCoroutinesApi
class FakeToDoRepository(private val scope: CoroutineScope) : ToDoRepository {

    private val toDos = ConcurrentHashMap<Long, ToDoModel>()
    private var todosChannel = Channel<List<ToDoModel>>()
    private val generateValuesJob: Job
    private val _connectionStatusLiveData = MutableLiveData<Boolean>()

    // generate infinite amount of elements, but throw exception after 10th element
    init {
        generateValuesJob = scope.launch {
            generateValues().collect { model ->
                saveModel(model)
                publishChanges()

                closeChannelIfLimitReached()
            }
        }
    }

    override val connectionStatus: LiveData<Boolean>
        get() = _connectionStatusLiveData

    override fun fetchToDos(): Flow<List<ToDoModel>> {
        if (todosChannel.isClosedForSend) {
            todosChannel = Channel()
            todosChannel.offer(toDos.values.toList())
            _connectionStatusLiveData.postValue(true)
        }
        return todosChannel.receiveAsFlow()
    }

    override suspend fun addToDo(title: String, description: String) {
        delay(DELAY_FOR_TODO_OPERATION)
        throwIfToDoListLimitReached()
        createAndSaveModel(title, description)
        publishChanges()
    }

    override fun completeToDo(uniqueId: Long) {
        throwIfIdIsEven(uniqueId)
        // if such model exists update it's completion date
        val model = toDos[uniqueId]?.copy(completedAt = Date())
        // or do nothing at all
                ?: return
        saveModel(model)
        publishChanges()
    }

    // generates infinite flow of values
    private fun generateValues() = flow {
        while (true) {
            delay(DELAY_OF_VALUES_GENERATOR)
            val model = createToDo(idGenerator.getAndIncrement())
            emit(model)
        }
    }

    private fun publishChanges() {
        scope.launch {
            if (!todosChannel.isClosedForSend) {
                todosChannel.offer(toDos.values.toList())
            }
        }
    }

    private fun createToDo(
        id: Long,
        title: String = "ToDo №$id",
        description: String = "Awesome stuff to do!"
    ) = ToDoModel(
            uniqueId = id,
            title = title,
            description = description
    )

    private fun saveModel(model: ToDoModel) {
        toDos[model.uniqueId] = model
    }

    private fun createAndSaveModel(title: String, description: String) {
        saveModel(createToDo(idGenerator.getAndIncrement(), title, description))
    }

    /**
     * Throws error if [uniqueId] is even but only when [uniqueId] > 2.
     */
    private fun throwIfIdIsEven(uniqueId: Long) {
        if (uniqueId <= 2 || uniqueId % 2 != 0L) {
            return
        }
        throw UnsupportedOperationException("You died")
    }

    private fun throwIfToDoListLimitReached() {
        if (toDos.size < TODOS_ITEMS_TO_THROW) {
            return
        }
        throw ConnectException("You died")
    }

    private fun closeChannelIfLimitReached() {
        if (toDos.size < TODO_ITEMS_CHANNEL_TO_THROW) {
            return
        }
        scope.launch {
            todosChannel.close(IllegalArgumentException("You died"))
            generateValuesJob.cancelAndJoin()
            _connectionStatusLiveData.postValue(false)
        }
    }

    override suspend fun deleteToDo(uniqueId: Long) {
        delay(DELAY_FOR_TODO_OPERATION)
        toDos.remove(uniqueId)
        throwIfToDoListLimitReached()
        publishChanges()
    }
}
