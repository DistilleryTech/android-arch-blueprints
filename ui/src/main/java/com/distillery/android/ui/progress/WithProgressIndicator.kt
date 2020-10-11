package com.distillery.android.ui.progress

/**
 * Interface for any class that supports loading indicator display feature.
 */
interface WithProgressIndicator {
    fun displayProgress()
    fun hideProgress()
}
