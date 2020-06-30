package com.distillery.android.blueprints.mvi.todo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.distillery.android.blueprints.mvi.R
import com.distillery.android.blueprints.mvi.todo.TodoIntent
import com.distillery.android.blueprints.mvi.todo.TodoListModel
import com.distillery.android.blueprints.mvi.todo.state.ConfirmationCode
import com.distillery.android.blueprints.mvi.todo.state.TodoState
import com.distillery.android.blueprints.mvi.todo.viewmodel.TodoViewModel
import com.distillery.android.ui.adapter.ToDoListAdapter
import com.distillery.android.ui.databinding.FragmentTodoBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.lifecycle.lifecycleScope as viewCoroutineScope

@ExperimentalCoroutinesApi
class TodoListFragment : Fragment() {

    private lateinit var binding: FragmentTodoBinding
    private val todoViewModel: TodoViewModel by viewModel()
    private val todoListAdapter: ToDoListAdapter by lazy { getToDoListAdapter() }
    private val completedListAdapter: ToDoListAdapter by lazy { getToDoListAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgress()
        setUpListeners()
        setUpObservers()
        setUpTodoLists()
        setUpCompleteTodoList()
    }

    private fun setUpCompleteTodoList() {
        binding.completedTodoList.adapter = completedListAdapter
        binding.completedTodoList.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun setUpTodoLists() {
        binding.todoList.adapter = todoListAdapter
        binding.todoList.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun setUpObservers() {
        todoViewModel.proccessIntents(flow {
            emit(TodoIntent.PopulateTodoList)
        })
        todoViewModel.todoState
                .onEach { state -> handleState(state) }
                .launchIn(viewCoroutineScope)
    }

    private fun setUpListeners() {
        binding.buttonAdd.setOnClickListener {
            navigateToAddItemFragment()
        }
    }

    private fun handleState(state: TodoState<TodoListModel>) {
        when (state) {
            is TodoState.LoadingState -> {
                showProgress()
            }
            is TodoState.DataState -> {
                hideProgress()
                todoListAdapter.submitList(state.todoListFlow.todoList)
                completedListAdapter.submitList(state.todoListFlow.completedTodoList)
            }
            is TodoState.ErrorState -> {
                hideProgress()
                showSnackbarMessage(state.errorMsg?.message)
            }
            is TodoState.ConfirmationState -> {
                proccessConfirmation(state.confirmationCode)
            }
        }
    }

    private fun proccessConfirmation(confirmation: ConfirmationCode) {
        when (confirmation) {
            is ConfirmationCode.UPDATED -> {
                showSnackbarMessage(getString(R.string.update_todo_list_message))
            }
            is ConfirmationCode.DELETED -> {
                showSnackbarMessage(getString(R.string.deleted_todo_item_message))
            }
        }
    }

    private fun showProgress() {
        binding.progressBar.isVisible = true
    }

    private fun hideProgress() {
        binding.progressBar.isVisible = false
    }

    private fun showSnackbarMessage(message: String?) {
        Snackbar.make(
                binding.todoContent,
                message ?: getString(R.string.genericError),
                Snackbar.LENGTH_SHORT)
                .show()
    }

    private fun navigateToAddItemFragment() {
        parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, AddTodoItemFragment.newInstance())
                .commit()
    }

    private fun getToDoListAdapter(): ToDoListAdapter {
        return ToDoListAdapter(
                onDeleteClickListener= {
            todoViewModel.proccessIntents(flow {
                emit(TodoIntent.DeleteTodo(it.uniqueId))
            })},
                onCompleteClickListener = {
            todoViewModel.proccessIntents(flow {
                emit(TodoIntent.CompleteTodo(it.uniqueId))
            })
        })
    }

    companion object {
        fun instance() = TodoListFragment()
    }
}
