package com.distillery.android.domain

import com.distillery.android.domain.models.ToDoModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Fraction of time in ms to allow suspend method to kick in.
 * Should be veeery small.
 */
private const val FRACTION_OF_TIME = 100L

private const val EXPECTED_AMOUNT_OF_ELEMENTS = 2

@ExperimentalCoroutinesApi
class FakeToDoRepositoryTest {

    private lateinit var repository: ToDoRepository

    @BeforeEach
    fun setup() {
        repository = FakeToDoRepository(scope = GlobalScope)
    }

    @Test
    fun `repository generates values on its own every 5 seconds`() = runBlocking {
        // given we subscribed to repository changes
        var listSize = 0
        val subscription = launch {
            repository.fetchToDos().collect {
                listSize = it.size
            }
        }
        // and initially list size is zero
        check(listSize == 0)

        // when enough time has past
        delay(DELAY_OF_VALUES_GENERATOR * EXPECTED_AMOUNT_OF_ELEMENTS + FRACTION_OF_TIME)

        // then list has 2 element
        assertEquals(EXPECTED_AMOUNT_OF_ELEMENTS, listSize)

        subscription.cancel()
    }

    @Test
    fun `repository add values after 1_5 seconds`() = runBlocking {
        // given we subscribed to repository changes
        var listSize = 0
        val subscription = launch {
            repository.fetchToDos().collect {
                listSize = it.size
            }
        }
        // and initially list size is zero
        check(listSize == 0)

        // when we add element
        launch {
            repository.addToDo("stub", "stub")
        }
        // and delay is past
        delay(DELAY_FOR_TODO_OPERATION + FRACTION_OF_TIME)

        // then list has 1 element
        assertEquals(1, listSize)

        subscription.cancel()
    }

    @Test
    fun `repository delete values after 1_5 seconds`() = runBlocking {
        var listSize = 0
        val subscription = launch {
            repository.fetchToDos().collect {
                listSize = it.size
            }
        }
        check(listSize == 0)

        // when enough time has passed 2 elements were added to list
        delay(DELAY_OF_VALUES_GENERATOR * EXPECTED_AMOUNT_OF_ELEMENTS + FRACTION_OF_TIME)

        // when we delete element
        repository.deleteToDo(0)

        // and delay is past
        delay(FRACTION_OF_TIME)

        // then list has 1 element
        assertEquals(1, listSize)

        subscription.cancel()
    }

    @Test
    fun `repository completes objects immediately`() = runBlocking {
        // given list has 1 element
        var listOfTodos = emptyList<ToDoModel>()
        val subscription = launch {
            repository.addToDo("stub", "stub")
            repository.fetchToDos().collect {
                listOfTodos = it
            }
        }
        delay(DELAY_FOR_TODO_OPERATION + FRACTION_OF_TIME)
        check(listOfTodos.size == 1)
        // and no elements are completed
        assertTrue(listOfTodos.all { it.completedAt == null })

        // when we complete element
        repository.completeToDo(listOfTodos.first().uniqueId)

        // then list has 1 element and all elements are completed
        delay(FRACTION_OF_TIME) // minimal delay required in order to allow suspend methods to kick in
        assertEquals(1, listOfTodos.size)
        assertTrue(listOfTodos.all { it.completedAt != null })

        subscription.cancel()
    }
}
