package com.distillery.android.blueprints.mvvm

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

@ExperimentalCoroutinesApi
class MainCoroutineRule : TestRule {

    private var _testDispatcher: TestCoroutineDispatcher? = null
    val testDispatcher: TestCoroutineDispatcher
        get() = _testDispatcher ?: error("dispatcher is not initiated")

    override fun apply(base: Statement, description: Description): Statement = object : Statement() {
        override fun evaluate() {
            _testDispatcher = TestCoroutineDispatcher()
            Dispatchers.setMain(testDispatcher)
            base.evaluate()
            Dispatchers.resetMain()
            testDispatcher.cleanupTestCoroutines()
            _testDispatcher = null
        }
    }

    operator fun invoke(block: suspend TestCoroutineScope.() -> Unit) {
        testDispatcher.runBlockingTest(block)
    }
}
