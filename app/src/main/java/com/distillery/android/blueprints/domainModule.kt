package com.distillery.android.blueprints

import com.distillery.android.data.room.ToDoDataSource
import com.distillery.android.data.room.dao.ToDoDao
import com.distillery.android.data.room.dao.ToDoDatabase
import com.distillery.android.data.room.mapper.TodoDataToDomainMapper
import com.distillery.android.domain.ToDoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val domainModule = module {

    factory<TodoDataToDomainMapper>() { TodoDataToDomainMapper() }
    single<ToDoDatabase> { ToDoDatabase.create(get()) }
    factory<ToDoDao> { get<ToDoDatabase>().dao() }
    factory<ToDoDataSource> { ToDoDataSource(get(), get()) }
    factory<ToDoRepository> { get<ToDoDataSource>() }
}
