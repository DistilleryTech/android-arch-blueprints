package com.distillery.android.blueprints.mvi.todo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.distillery.android.blueprints.mvi.R
import com.distillery.android.blueprints.mvi.todo.AddTodoIntent
import com.distillery.android.blueprints.mvi.todo.TodoListModel
import com.distillery.android.blueprints.mvi.todo.state.TodoState
import com.distillery.android.blueprints.mvi.todo.viewmodel.AddTodoViewModel
import com.distillery.android.ui.databinding.FragmentAddTodoItemBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import androidx.lifecycle.lifecycleScope as viewCoroutineScope

@ExperimentalCoroutinesApi
class AddTodoItemFragment : Fragment() {

    lateinit var binding: FragmentAddTodoItemBinding
    private val addTodoViewModel: AddTodoViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddTodoItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpListeners()
        setUpObservers()
    }

    private fun setUpObservers() {
        addTodoViewModel.todoState.onEach { state -> handleState(state) }
                .launchIn(viewCoroutineScope)
    }

    private fun setUpListeners() {
        binding.saveFabButton.setOnClickListener {
            addTodoViewModel.proccessIntents(flow {
                val title = binding.addTitleEditText.text.toString()
                val description = binding.addDescriptionEditText.text.toString()
                emit(AddTodoIntent.SaveTodo(title, description))
            })
        }
    }

    private fun handleState(state: TodoState<TodoListModel>) {
        when (state) {
            is TodoState.ConfirmationState -> {
                showSnackbarMessage(getString(R.string.item_added))
                parentFragmentManager.popBackStack()
            }
            is TodoState.ErrorState -> {
                showSnackbarMessage(state.errorMsg?.message)
            }
        }
    }

    private fun showSnackbarMessage(message: String?) {
        Snackbar.make(
                binding.addTodoContent,
                message ?: getString(R.string.genericError),
                Snackbar.LENGTH_SHORT)
                .show()
    }

    companion object {
        fun newInstance() = AddTodoItemFragment()
    }
}
