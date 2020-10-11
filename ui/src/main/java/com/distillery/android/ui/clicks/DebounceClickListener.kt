package com.distillery.android.ui.clicks

import android.view.View

/**
 * This class allows a single click and prevents multiple clicks on
 * the same button in rapid succession. This may be useful if you need to prevent too fast user clicks.
 * Default delay between clicks is 750ms.
 */
open class DebounceClickListener(
    private val clickListener: View.OnClickListener?
) : DebounceEventListener(), View.OnClickListener {

    constructor(clickListener: () -> Unit) : this(View.OnClickListener { clickListener() })

    final override fun onClick(v: View?) {
        if (shouldBeConsumed()) {
            onOneOffClick(v)
        }
    }

    open fun onOneOffClick(v: View?) {
        clickListener?.onClick(v)
    }
}

fun View.setDebounceClickListener(clickListener: () -> Unit) {
    setOnClickListener(DebounceClickListener { clickListener.invoke() })
}
