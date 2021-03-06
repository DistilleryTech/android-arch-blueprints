package main.presenter

import androidx.lifecycle.LifecycleOwner
import com.distillery.android.blueprints.mvp.todo.contract.TodoContract
import com.distillery.android.domain.ToDoRepository
import com.distillery.android.domain.models.ToDoModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.flow
import main.mvpModule
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.never
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.util.Date

class PresenterImplementationTest : KoinTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(mvpModule)
    }

    // required to make your Mock via Koin
    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mock(clazz.java)
    }

    lateinit var viewContract: TodoContract.View

    @InternalCoroutinesApi
    @Test
    fun `declareMock with KoinTest`() {
        val expected = listOf(
                ToDoModel(3L, "title", "description", Date())
        )
        declareMock<ToDoRepository> {
            // do your given behavior here
            given(this.fetchToDos()).willReturn(
                    flow {
                        emit(expected)
                    }
            )
        }
        viewContract = mock(TodoContract.View::class.java)
        val presenter = PresenterImplementation(mock(LifecycleOwner::class.java), viewContract)
        presenter.startFlow()

        verify(viewContract, never()).showError(anyInt())
    }
}
