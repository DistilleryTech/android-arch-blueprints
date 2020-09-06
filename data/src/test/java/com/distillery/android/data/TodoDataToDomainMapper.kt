package com.distillery.android.data

import com.distillery.android.data.room.mapper.TodoDataToDomainMapper
import com.distillery.android.data.room.models.ToDoEntity
import com.distillery.android.domain.models.ToDoModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Date

private val DATA_MODEL = ToDoEntity(
    uniqueId = 1,
    title = "title",
    description = "description",
    createdAt = Date().time - 1000,
    completedAt = Date().time - 500

)
private val EXPECTED_DOMAIN_MODEL = ToDoModel(
    uniqueId = DATA_MODEL.uniqueId,
    title = DATA_MODEL.title,
    description = DATA_MODEL.description,
    createdAt = Date(DATA_MODEL.createdAt),
    completedAt = DATA_MODEL.completedAt?.let { Date(it) }
)

class TodoDataToDomainMapperTest {
    private val mapper = TodoDataToDomainMapper()

    @Test
    fun mappingIsCorrect() {
        assertEquals(EXPECTED_DOMAIN_MODEL, mapper.map(DATA_MODEL))
    }
}
