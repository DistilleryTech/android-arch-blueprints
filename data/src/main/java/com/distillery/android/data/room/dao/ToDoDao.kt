package com.distillery.android.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.distillery.android.data.room.models.ToDoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Query("SELECT * FROM ToDoEntity")
    fun fetchEverything(): Flow<List<ToDoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(entity: ToDoEntity)

    @Query("UPDATE ToDoEntity SET completedAt = :timestamp  WHERE uniqueId = :uniqueId")
    fun completeToDo(timestamp: Long, uniqueId: Long)

    @Query("DELETE FROM ToDoEntity WHERE uniqueId = :uniqueId")
    fun deleteById(uniqueId: Long)
}
