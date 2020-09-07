package main.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.LinearLayoutManager
import com.distillery.android.blueprints.mvp.todo.contract.TodoContract
import com.distillery.android.domain.models.ToDoModel
import com.distillery.android.ui.databinding.FragmentTodoBinding
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class TodoFragment : Fragment(),
    TodoContract.View {
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerPendingAdapter: TodoListAdapter
    private lateinit var recyclerDoneAdapter: TodoListAdapter
    private lateinit var presenter: TodoContract.Presenter
    private lateinit var view: TodoContract.View
    val presenterImpl: TodoContract.Presenter by inject { parametersOf(this, view) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = this
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
    @Suppress("LongMethod")
    private fun initPendingAndDoneRecyclerViews() {
        binding.todoList.apply {
            layoutManager = LinearLayoutManager(this@TodoFragment.context)
            recyclerPendingAdapter = TodoListAdapter(
                object : TodoListAdapter.CheckBoxOnClickListener {
                    override fun onClick(item: ToDoModel, newState: Boolean) =
                        presenter.onClickCheckboxCompletion(item)
                },
                object : TodoListAdapter.DeleteMarkOnClickListener {
                    override fun onClick(item: ToDoModel) {
                        presenter.onClickDeleteTask(item)
                    }
                }
            )
            adapter = recyclerPendingAdapter
        }
        binding.completedTodoList.apply {
            layoutManager = LinearLayoutManager(this@TodoFragment.context)
            recyclerDoneAdapter = TodoListAdapter(
                object : TodoListAdapter.CheckBoxOnClickListener {
                    override fun onClick(
                        item: ToDoModel,
                        newState: Boolean
                    ) = Unit
                },
                object : TodoListAdapter.DeleteMarkOnClickListener {
                    override fun onClick(item: ToDoModel) {
                        presenter.onClickDeleteTask(item)
                    }
                }
            )
            adapter = recyclerDoneAdapter
        }
        binding.buttonAdd.setOnClickListener {
            presenter.onClickAddTask()
        }
        val presenterImpl: TodoContract.Presenter by inject { parametersOf(this, this) }
        this.presenter = presenterImpl

        lifecycle.addObserver(presenterImpl as LifecycleObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // prevent memory leaks
        binding.todoList.adapter = null
        _binding = null
    }

    companion object {
        private val TAG = TodoFragment::class.qualifiedName ?: "TodoFragment"
        @JvmStatic
        fun newInstance() = TodoFragment()
    }

    @Suppress("EmptyFunctionBlock")
    override fun showError(message: String) {
        Log.e(TAG, message)
    }

    @Suppress("EmptyFunctionBlock")
    override fun notifyTaskDeleted() { }

    override fun showPendingTasks(tasks: List<ToDoModel>) {
        recyclerPendingAdapter.submitList(tasks)
    }

    override fun showDoneTasks(tasks: List<ToDoModel>) {
        recyclerDoneAdapter.submitList(tasks)
    }
}
