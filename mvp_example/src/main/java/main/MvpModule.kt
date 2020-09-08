package main

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.distillery.android.blueprints.mvp.todo.contract.TodoContract
import com.distillery.android.domain.FakeToDoRepository
import com.distillery.android.domain.ToDoRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import org.koin.core.qualifier.named
import main.presenter.PresenterImplementation
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val mvpModule = module {
    factory(named(TAG)) {
        val job = Job()
        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            Log.d(TAG, "Error: ${throwable.message!!}")
            throwable.printStackTrace()
        }
        CoroutineScope(Dispatchers.IO + job + errorHandler)
    }
    single<ToDoRepository>(named(TAG)) { FakeToDoRepository(get(named(TAG))) }
    single<TodoContract.Presenter> { (lifecycleOwner: LifecycleOwner, view: TodoContract.View) ->
        PresenterImplementation(lifecycleOwner, view)
    }
}

private const val TAG = "MVP" // for named scope to avoid conflicts between modules
