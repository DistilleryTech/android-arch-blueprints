package com.distillery.android.blueprints.mvi.todo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.distillery.android.ui.databinding.FragmentAddTodoItemBinding

class AddTodoItemFragment : Fragment() {

    lateinit var binding: FragmentAddTodoItemBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddTodoItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = AddTodoItemFragment()
    }
}
