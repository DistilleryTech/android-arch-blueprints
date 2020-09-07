package com.distillery.android.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ToDoEntity(
    /**
     * Unique identifier.
     */
    @PrimaryKey(autoGenerate = true)
    val uniqueId: Long = 0,
    /**
     * Main goal of the To Do.
     */
    val title: String,
    /**
     * Detailed explanation of the To Do action.
     */
    val description: String,
    /**
     * Date of the creation. Milliseconds.
     */
    val createdAt: Long,
    /**
     * Date of the completion. Milliseconds.
     */
    val completedAt: Long? = null
)
