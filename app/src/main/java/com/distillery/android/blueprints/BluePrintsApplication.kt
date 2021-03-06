package com.distillery.android.blueprints

import android.app.Application
import com.distillery.android.blueprints.mvi.mviModule
import com.distillery.android.blueprints.mvvm.todo.utils.mvvmModule
import main.mvpModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BluePrintsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
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
