package com.distillery.android.domain

import com.distillery.android.domain.models.ToDoModel
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.Date

class ToDoModelTest {

    @Test
    fun `isCompleted return true if ToDoModel contain completed date`() {
        val testModel = ToDoModel(0, "", "", completedAt = Date())
        assertTrue(testModel.isCompleted)
    }

    @Test
    fun `isCompleted return false if completed date doesn't exist in ToDoModel`() {
        val testModel = ToDoModel(0, "", "")
        assertFalse(testModel.isCompleted)
    }
}
