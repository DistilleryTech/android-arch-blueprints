package com.distillery.android.blueprints

import android.app.Application
import com.distillery.android.blueprints.mvi.mviModule
import com.distillery.android.blueprints.mvvm.mvvmModule
import com.distillery.android.domain.FakeToDoRepository
import com.distillery.android.domain.ToDoRepository
import di.mvpModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class BluePrintsApplication : Application() {

    private val commonModules = module {
        factory { CoroutineScope(Dispatchers.IO + SupervisorJob()) }
        single<ToDoRepository> { FakeToDoRepository(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.INFO)
            androidContext(this@BluePrintsApplication)
            modules(listOf(
                    commonModules,
                    mvvmModule,
                    mviModule,
                    mvpModule
            ))
        }
    }
}
