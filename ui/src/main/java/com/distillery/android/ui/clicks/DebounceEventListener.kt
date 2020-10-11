package com.distillery.android.ui.clicks

private const val DEFAULT_ACTION_DELAY_MS = 750L

/**
 * Allows single event and prevents multiple events on the same view in rapid succession.
 * This may be useful if you need to prevent too fast view clicks, for example.
 *
 * Default delay between events is 750ms. Only one event within this [delay] time will processed while others
 * will dropped. After the [delay] timeout next one event will allowed to process and so on.
 */
open class DebounceEventListener {
    /** Minimal delay between allowed events. Only one event within this time will processed */
    var delay: Long = DEFAULT_ACTION_DELAY_MS
    private var lastActionTimeMs = 0L

    /**
     * Returns true if event should be consumed, false if this event should be dropped because
     * it fired too close to previous one event.
     */
    protected fun shouldBeConsumed(): Boolean {
        val currentTimeMs = System.currentTimeMillis()
        return if (currentTimeMs - lastActionTimeMs > delay) {
            lastActionTimeMs = currentTimeMs
            true
        } else {
            false
        }
    }
}
