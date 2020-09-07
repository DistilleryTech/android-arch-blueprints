package com.distillery.android.blueprints

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

internal inline fun <reified T : AppCompatActivity> AppCompatActivity.startActivity(block: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    block(intent)
    startActivity(intent)
}
