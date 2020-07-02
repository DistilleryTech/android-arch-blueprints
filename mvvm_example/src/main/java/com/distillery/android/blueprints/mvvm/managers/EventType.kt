package com.distillery.android.blueprints.mvvm.managers

import com.distillery.android.mvvm_example.R

enum class EventType(val stringResId: Int) {
    ADD(R.string.add_message),
    DELETE(R.string.delete_message),
    COMPLETE(R.string.complete_message),
    UNSUPPORTED_OPERATION(R.string.error_message_unsupported),
    CONNECTION_FAILED(R.string.error_message_connection_Failed);
}
