package com.distillery.android.blueprints.mvvm.todo.viewmodel.rules

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler

class LiveDataRule : AfterEachCallback, BeforeEachCallback, TestExecutionExceptionHandler {
    private val errorList = ArrayList<Throwable>()
    private val taskExecutor = object : TaskExecutor() {
        override fun executeOnDiskIO(runnable: Runnable): Unit = runnable.run()
        override fun postToMainThread(runnable: Runnable): Unit = runnable.run()
        override fun isMainThread(): Boolean = true
    }

    override fun beforeEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(taskExecutor)
    }

    override fun handleTestExecutionException(context: ExtensionContext?, throwable: Throwable) {
        errorList.add(throwable)
    }

    override fun afterEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(null)
        val errorListCopy = ArrayList(errorList)
        errorList.clear()

        assertTrue(errorListCopy.isEmpty()) {
            errorListCopy
                .asSequence()
                .mapTo(mutableListOf(), Throwable::toString)
                .joinToString { it }
        }
    }
}