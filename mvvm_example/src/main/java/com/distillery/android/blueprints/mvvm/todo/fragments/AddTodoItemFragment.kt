package com.distillery.android.blueprints.mvvm.todo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.distillery.android.blueprints.mvvm.todo.viewmodel.TodoListViewModel
import com.distillery.android.ui.databinding.FragmentAddTodoItemBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AddTodoItemFragment : Fragment() {
    private val todoViewModel: TodoListViewModel by sharedViewModel()
    private var _binding: FragmentAddTodoItemBinding? = null
    private val binding get() = _binding!! // use this property only between onCreateView and onDestroyView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTodoItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveFabButton.setOnClickListener {
            val title = binding.addTitleEditText.text.toString()
            val description = binding.addDescriptionEditText.text.toString()
            if (title.isNotBlank()) {
                todoViewModel.addTodo(title, description)
            }
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): Fragment {
            return AddTodoItemFragment()
        }
    }
}
