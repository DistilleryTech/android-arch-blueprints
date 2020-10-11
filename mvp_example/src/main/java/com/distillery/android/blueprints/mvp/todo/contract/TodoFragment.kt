package com.distillery.android.blueprints.mvp.todo.contract

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.distillery.android.domain.models.ToDoModel
import com.distillery.android.mvp_example.R
import com.distillery.android.ui.adapter.ToDoListAdapter
import com.distillery.android.ui.databinding.FragmentTodoBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf

class TodoFragment : Fragment(), TodoContract.View {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerPendingAdapter: ToDoListAdapter
    private lateinit var recyclerDoneAdapter: ToDoListAdapter

    private val presenter: TodoContract.Presenter = get { parametersOf(this) }

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
        initPendingAndDoneRecyclerViews()
    }

    private fun initPendingAndDoneRecyclerViews() {
        binding.todoList.apply {
            layoutManager = LinearLayoutManager(this@TodoFragment.context)
            adapter = initPendingItemsAdapter()
        }
        binding.completedTodoList.apply {
            layoutManager = LinearLayoutManager(this@TodoFragment.context)
            adapter = initDoneItemsAdapter()
        }
        binding.buttonAdd.setOnClickListener {
            presenter.onClickAddTask()
        }
    }

    private fun initPendingItemsAdapter(): ToDoListAdapter {
        recyclerPendingAdapter = ToDoListAdapter(
                onCompleteClickListener = {
                    presenter.onClickCheckboxCompletion(it)
                },
                onDeleteClickListener = {
                    presenter.onClickDeleteTask(it)
                })
        return recyclerPendingAdapter
    }

    private fun initDoneItemsAdapter(): ToDoListAdapter {
        recyclerDoneAdapter = ToDoListAdapter(
                onCompleteClickListener = {
                    presenter.onClickCheckboxCompletion(it)
                },
                onDeleteClickListener = {
                    presenter.onClickDeleteTask(it)
                })
        return recyclerDoneAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // prevent memory leaks
        binding.todoList.adapter = null
        binding.completedTodoList.adapter = null
        _binding = null
    }

    override fun showLoadingError() {
        Snackbar.make(binding.bar, getString(R.string.error_message_loading_items), Snackbar.LENGTH_SHORT)
                .show()
    }

    override fun showCompletingError() {
        Snackbar.make(binding.bar, getString(R.string.error_message_completing_item), Snackbar.LENGTH_SHORT)
                .show()
    }

    override fun showAddingItemError() {
        Snackbar.make(binding.bar, getString(R.string.error_message_adding_item), Snackbar.LENGTH_SHORT)
                .show()
    }

    override fun showPendingTasks(tasks: List<ToDoModel>) {
        recyclerPendingAdapter.submitList(tasks)
    }

    override fun showDoneTasks(tasks: List<ToDoModel>) {
        recyclerDoneAdapter.submitList(tasks)
    }

    override fun displayProgress() {
       binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }
}
