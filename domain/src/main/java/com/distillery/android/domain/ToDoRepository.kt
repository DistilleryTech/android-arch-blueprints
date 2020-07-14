package com.distillery.android.domain

import androidx.lifecycle.LiveData
import com.distillery.android.domain.models.ToDoModel
import kotlinx.coroutines.flow.Flow
import java.net.ConnectException

interface ToDoRepository {
    /**
     * this live data represents the repo connection status with boolean type
     * Active = true, InActive = false by listening to this live data you could
     * able to reconnect with the repo data stream
     */
    val connectionStatus: LiveData<Boolean>

    /**
     * Fetches news To Do items.
     * New item appears from time to time.
     * @throws IllegalArgumentException when flow is closed due to inner error
     */
    fun fetchToDos(): Flow<List<ToDoModel>>

    /**
     * Creates a new To Do action asynchronously and adds it to the existing list of To Dos.
     * @throws ConnectException when new value creation failed
     */
    suspend fun addToDo(title: String, description: String)

    /**
     * Mark To Do with [uniqueId] as completed synchronously.
     * @throws UnsupportedOperationException when complete To Do with even id
     */
    fun completeToDo(uniqueId: Long)

    /**
     * Deletes a To Do action asynchronously and deletes it from the existing list of To Dos.
     */
    suspend fun deleteToDo(uniqueId: Long)
}
