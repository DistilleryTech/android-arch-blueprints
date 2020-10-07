package main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.LinearLayoutManager
import com.distillery.android.blueprints.mvp.todo.contract.TodoContract
import com.distillery.android.domain.models.ToDoModel
import com.distillery.android.ui.databinding.FragmentTodoBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class TodoFragment : Fragment(), TodoContract.View {
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    // TODO adapters are not required to be variables, access through recycler view
    //  Q (A.Rudometkin, 30.09.2020): Does it really matter? Seems like we use their interface below
    private lateinit var recyclerPendingAdapter: TodoListAdapter
    private lateinit var recyclerDoneAdapter: TodoListAdapter
    private val presenter: TodoContract.Presenter by inject { parametersOf(this, this) }

    private val itemMarkClickListener = object : TodoListAdapter.CheckBoxOnClickListener {
        override fun onClick(item: ToDoModel, newState: Boolean) =
                presenter.onClickCheckboxCompletion(item)
    }

    private val itemDeleteClickListener = object : TodoListAdapter.DeleteMarkOnClickListener {
        override fun onClick(item: ToDoModel) {
            presenter.onClickDeleteTask(item)
        }
    }

    private val itemEmptyClickListener = object : TodoListAdapter.CheckBoxOnClickListener {
        override fun onClick(item: ToDoModel, newState: Boolean) = Unit
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoBinding.inflate(
                inflater,
                container,
                false
        )
        return binding.root
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPendingAndDoneRecyclerViews()
    }

    @InternalCoroutinesApi
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

        lifecycle.addObserver(presenter as LifecycleObserver)
    }

    private fun initPendingItemsAdapter(): TodoListAdapter {
        recyclerPendingAdapter = TodoListAdapter(itemMarkClickListener, itemDeleteClickListener)
        return recyclerPendingAdapter
    }

    private fun initDoneItemsAdapter(): TodoListAdapter {
        recyclerDoneAdapter = TodoListAdapter(itemEmptyClickListener, itemDeleteClickListener)
        return recyclerDoneAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // prevent memory leaks
        binding.todoList.adapter = null
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = TodoFragment()
    }

    override fun showError(message: Int) {
        Snackbar.make(binding.bar, getString(message), Snackbar.LENGTH_SHORT)
                .show()
    }

    override fun notifyTaskDeleted() = Unit

    override fun showPendingTasks(tasks: List<ToDoModel>) {
        recyclerPendingAdapter.submitList(tasks)
    }

    override fun showDoneTasks(tasks: List<ToDoModel>) {
        recyclerDoneAdapter.submitList(tasks)
    }
}
