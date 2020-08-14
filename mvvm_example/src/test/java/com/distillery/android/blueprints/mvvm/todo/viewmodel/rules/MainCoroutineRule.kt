package com.distillery.android.blueprints.mvvm.todo.viewmodel.rules

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

@ExperimentalCoroutinesApi
open class MainCoroutineRule : AfterEachCallback, BeforeEachCallback {
    private var dispatcher: TestCoroutineDispatcher? = null

    override fun beforeEach(context: ExtensionContext?) {
        dispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(dispatcher!!)
    }

    override fun afterEach(context: ExtensionContext?) {
        Dispatchers.resetMain()
        dispatcher?.cleanupTestCoroutines()
        dispatcher = null
    }

    operator fun invoke(block: suspend TestCoroutineScope.() -> Unit) {
        dispatcher?.runBlockingTest(block)
    }
}
