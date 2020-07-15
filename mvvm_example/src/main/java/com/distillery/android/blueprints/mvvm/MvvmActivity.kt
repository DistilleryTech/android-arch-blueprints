package com.distillery.android.blueprints.mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.distillery.android.blueprints.mvvm.todo.fragments.ToDoListFragment
import com.distillery.android.ui.databinding.ActivityTodoBinding

class MvvmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, ToDoListFragment.newInstance())
            .commit()
    }
}
