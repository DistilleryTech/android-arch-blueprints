package com.distillery.android.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

/**
 * Represents `To Do` action item list.
 */
@Parcelize
data class ToDoModel(
    /**
     * Unique identifier.
     */
    val uniqueId: Long,
    /**
     * Main goal of the To Do.
     */
    val title: String,
    /**
     * Detailed explanation of the To Do action.
     */
    val description: String,
    /**
     * Date of the creation.
     */
    val createdAt: Date = Date(),
    /**
     * Date of the completion.
     */
    val completedAt: Date? = null
) : Parcelable

val ToDoModel.isCompleted get() = completedAt != null
