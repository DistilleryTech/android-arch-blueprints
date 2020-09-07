package com.distillery.android.blueprints.mvvm.todo.viewmodel

import androidx.lifecycle.viewModelScope
import com.distillery.android.blueprints.mvvm.todo.utils.AppErrorHandler
import com.distillery.android.blueprints.mvvm.todo.utils.EventType
import com.distillery.android.blueprints.mvvm.todo.viewmodel.models.LiveDataTest
import com.distillery.android.blueprints.mvvm.todo.viewmodel.rules.LiveDataRule
import com.distillery.android.blueprints.mvvm.todo.viewmodel.rules.MainCoroutineRule
import com.distillery.android.domain.FakeToDoRepository
import com.distillery.android.domain.models.ToDoModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension

@ExperimentalCoroutinesApi
@ExtendWith(LiveDataRule::class)
class TodoListViewModelTest {

    @JvmField
    @RegisterExtension
    val coroutineRule = MainCoroutineRule()

    private lateinit var viewModel: TodoListViewModel
    private lateinit var todoListLiveDataTest: LiveDataTest<List<ToDoModel>>
    private lateinit var completedTodoListLiveDataTest: LiveDataTest<List<ToDoModel>>
    private lateinit var snackBarMessageLiveDataTest: LiveDataTest<EventType>

    @BeforeEach
    fun setup() {
        coroutineRule {
            viewModel = TodoListViewModel(FakeToDoRepository(this), AppErrorHandler())
            todoListLiveDataTest = LiveDataTest(viewModel.todoListLiveData)
            completedTodoListLiveDataTest = LiveDataTest(viewModel.completedTodoListLiveData)
            snackBarMessageLiveDataTest = LiveDataTest(viewModel.snackBarMessageLiveData)
        }
    }

    @Test
    fun `add Todo item`() {
        val title = "test title 1"
        val description = "test description 1"

        coroutineRule { viewModel.addTodo(title, description) }
        val find = viewModel.todoListLiveData.value?.find { it.title == title && it.description == description }

        assertNotNull(find)
        assertEquals(EventType.ADD, viewModel.snackBarMessageLiveData.value)
    }

    @Test
    fun `complete Todo item`() {
        val toDoModel = viewModel.todoListLiveData.value?.get(0) ?: throw AssertionError("null value")
        coroutineRule { viewModel.completeTodo(toDoModel) }
        val find = viewModel.completedTodoListLiveData.value?.find { it.uniqueId == toDoModel.uniqueId }

        assertNotNull(find)
        assertNotNull(find?.completedAt)
        assertEquals(EventType.COMPLETE, viewModel.snackBarMessageLiveData.value)
    }

    @Test
    fun `delete Todo item`() {
        val toDoModel = viewModel.todoListLiveData.value?.get(0) ?: throw AssertionError("null value")
        coroutineRule { viewModel.deleteTodo(toDoModel) }
        val find = viewModel.todoListLiveData.value?.find { it.uniqueId == toDoModel.uniqueId }

        assertNull(find)
        assertEquals(EventType.DELETE, viewModel.snackBarMessageLiveData.value)
    }

    @Test
    fun `adding more than 5 items should throw error message`() {
        coroutineRule {
            for (i in 0..5) viewModel.addTodo("title", "description")
        }

        assertEquals(EventType.CONNECTION_FAILED, viewModel.snackBarMessageLiveData.value)
    }

    @Test
    fun `completing even item above 2 throw error message`() {
        val toDoModel = viewModel.todoListLiveData.value?.get(4) ?: throw AssertionError("null value")
        coroutineRule { viewModel.completeTodo(toDoModel) }

        assertEquals(EventType.UNSUPPORTED_OPERATION, viewModel.snackBarMessageLiveData.value)
    }

    @AfterEach
    fun tearDown() {
        viewModel.viewModelScope.cancel()
        snackBarMessageLiveDataTest.removeObserver()
        todoListLiveDataTest.removeObserver()
        completedTodoListLiveDataTest.removeObserver()
    }
}
