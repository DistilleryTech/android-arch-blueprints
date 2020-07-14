package com.distillery.android.blueprints.mvvm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.distillery.android.blueprints.mvvm.viewmodels.TodoListViewModel
import com.distillery.android.domain.models.ToDoModel
import com.distillery.android.mvvm_example.R
import com.distillery.android.ui.adapter.ToDoListAdapter
import com.distillery.android.ui.databinding.FragmentTodoBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ToDoListFragment : Fragment() {
    // this  view model will be shared with  AddTodoFragment with Activity scope
    private val todoListViewModel: TodoListViewModel by sharedViewModel()
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!! // use this property only between onCreateView and onDestroyView
    private val todoListAdapter: ToDoListAdapter by lazy { getToDoListAdapter() }
    private val completedListAdapter: ToDoListAdapter by lazy { getToDoListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList(binding.todoList, todoListAdapter, todoListViewModel.todoListLiveData)
        setupList(binding.completedTodoList, completedListAdapter, todoListViewModel.completedTodoListLiveData)
        setDividerVisibility()
        binding.buttonAdd.setOnClickListener { navigateToAddItemFragment() }
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * adding observers for SnackBar and close Activity
     */
    private fun observeViewModel() {
        todoListViewModel.snackBarMessageLiveData.observe(viewLifecycleOwner, Observer { message ->
            showSnackBar(message.stringResId)
        })
    }

    /**
     * simple snackBar with "OK" button to dismiss
     * @param message string resource ID for message
     */
    private fun showSnackBar(@StringRes message: Int) {
        val snackBar = Snackbar.make(binding.coordinatorLayout, message, Snackbar.LENGTH_SHORT)
        snackBar.setAction(R.string.ok) { snackBar.dismiss() }
        snackBar.show()
    }

    /**
     * initializes RecyclerView with adapter, layout manager and observer
     * also sets up divider visibility on observer
     */
    private fun setupList(
        recyclerView: RecyclerView,
        listAdapter: ToDoListAdapter,
        liveData: LiveData<List<ToDoModel>>
    ) {
        recyclerView.adapter = listAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        liveData.observe(viewLifecycleOwner, Observer {
            listAdapter.submitList(it)
            setDividerVisibility()
        })
    }

    /**
     * sets the divider line visibility by checking completed and non completed list items
     * if the only of the both list is empty then it hides the line otherwise makes it visible
     */
    private fun setDividerVisibility() { // todo
        val isBothListEmpty = completedListAdapter.currentList.isEmpty() || todoListAdapter.currentList.isEmpty()
        binding.divider.visibility = (if (isBothListEmpty) View.GONE else View.VISIBLE)
    }

    /**
     * returns TodoList adapter instance. since its common to both completed and non completed list
     * this same fun can be used for both adapters
     */
    private fun getToDoListAdapter(): ToDoListAdapter {
        return ToDoListAdapter(
                onDeleteClickListener = todoListViewModel::deleteTodo,
                onCompleteClickListener = todoListViewModel::completeTodo
        )
    }

    /**
     * navigates to add item fragment using fragment transaction
     */
    private fun navigateToAddItemFragment() {
        parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, AddTodoItemFragment.newInstance())
                .commit()
    }

    companion object {
        fun newInstance(): Fragment {
            return ToDoListFragment()
        }
    }
}
