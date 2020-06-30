package com.distillery.android.blueprints.mvi.feature.todo

import com.distillery.android.blueprints.mvi.todo.state.TodoState
import com.distillery.android.blueprints.mvi.todo.usecases.GetToDoListUseCase
import com.distillery.android.domain.ToDoRepository
import com.distillery.android.domain.models.ToDoModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GetToDoListUseCaseTest : KoinTest {

    @Mock
    private lateinit var mockRepo: ToDoRepository

    private val getToDoListUseCase = GetToDoListUseCase()

    private val listTasks = listOf(ToDoModel(1, "task", "description"))

    @Before
    fun setUp() {
        startKoin {
            modules(
                    module {
                        single<ToDoRepository> { mockRepo }
                    }
            )
        }
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `retrieve DataState with ToDoList`() = runBlocking {
        val mockedFlow = flow {
            emit(listTasks)
        }
        val dataSate = TodoState.DataState(listTasks)
        Mockito.`when`(mockRepo.fetchToDos()).thenReturn(mockedFlow)
        val result = getToDoListUseCase.getToDoList()
        result.collect {
            Assert.assertEquals(dataSate, it)
            // TODO fix test
//                Assert.assertEquals(dataSate.todoListFlow.first(), (it as TodoState.DataState).todoListFlow.first())
        }
    }

    @After
    fun after() {
        stopKoin()
    }
}
