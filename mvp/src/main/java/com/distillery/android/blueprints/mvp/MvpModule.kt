package com.distillery.android.blueprints.mvp

import com.distillery.android.domain.FakeToDoRepository
import com.distillery.android.domain.ToDoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val mvpModule = module {
    factory { CoroutineScope(Dispatchers.IO + SupervisorJob()) }
    single<ToDoRepository> { FakeToDoRepository(get()) }
}
