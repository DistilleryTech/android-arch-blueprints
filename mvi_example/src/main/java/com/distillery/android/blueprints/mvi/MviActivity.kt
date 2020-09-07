package com.distillery.android.blueprints.mvi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.distillery.android.blueprints.mvi.todo.fragments.TodoListFragment
import com.distillery.android.ui.databinding.ActivityTodoBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MviActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
                .replace(binding.container.id, TodoListFragment.instance())
                .commit()
    }
}
