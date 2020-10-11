package com.distillery.android.blueprints.mvp.di

import com.distillery.android.blueprints.mvp.todo.contract.TodoContract
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.distillery.android.blueprints.mvp.todo.contract.TodoPresenter
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val mvpModule = module {
    single<TodoContract.Presenter> { (view: TodoContract.View) ->
        TodoPresenter(view)
    }
}
