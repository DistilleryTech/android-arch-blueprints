package com.distillery.android.blueprints.mvvm.todo.utils

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import java.net.ConnectException

class AppErrorHandler {

    /**
     * use this for log, analytics (like crashlytics )
     * or post ui error messages. in this case it only logs
     */
    val unCaughtExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.d("AppErrorHandler", exception.toString())
    }

    /**
     * this fun handles exception for the lambda.
     * @return null if there is no exception, otherwise
     *         proper error EventType for the exception type
     */
    inline operator fun invoke(block: () -> Unit): EventType? = try {
        block()
        null
    } catch (e: UnsupportedOperationException) {
        EventType.UNSUPPORTED_OPERATION
    } catch (e: ConnectException) {
        EventType.CONNECTION_FAILED
    }
}
