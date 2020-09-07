package com.distillery.android.data.room.mapper

import com.distillery.android.data.room.models.ToDoEntity
import com.distillery.android.domain.models.ToDoModel
import java.util.Date

/**
 * Converts database entities to domain level models.
 */
class TodoDataToDomainMapper {

    fun map(data: ToDoEntity): ToDoModel {
        return ToDoModel(
            uniqueId = data.uniqueId,
            title = data.title,
            description = data.description,
            createdAt = Date(data.createdAt),
            completedAt = data.completedAt?.let { Date(it) }
        )
    }
}
