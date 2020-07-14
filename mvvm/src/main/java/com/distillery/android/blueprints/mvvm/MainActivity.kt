package com.distillery.android.blueprints.mvvm

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: ExampleViewModel by viewModels()

        viewModel.getData().observe(this, Observer {
            TODO("update UI using the received data")
        })
    }
}
