package com.distillery.android.blueprints

import android.app.Application
import com.distillery.android.blueprints.mvi.mviModule
import com.distillery.android.blueprints.mvp.mvpModule
import com.distillery.android.blueprints.mvvm.todo.utils.mvvmModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class BluePrintsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.INFO)
            androidContext(this@BluePrintsApplication)
            modules(
                listOf(
                    domainModule,
                    mvvmModule,
                    mviModule,
                    mvpModule
                )
            )
        }
    }
}
