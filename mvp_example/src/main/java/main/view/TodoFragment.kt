package main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.distillery.android.blueprints.mvp.todo.contract.TodoContract
import com.distillery.android.domain.models.ToDoModel
import com.distillery.android.mvp_example.R
import com.distillery.android.ui.databinding.FragmentTodoBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.InternalCoroutinesApi
import main.presenter.PresenterImplementation

class TodoFragment : Fragment(),
    TodoContract.View
{
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerPendingAdapter: TodoListAdapter
    private lateinit var recyclerDoneAdapter: TodoListAdapter
    private lateinit var presenter: TodoContract.Presenter

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
        val presenterImpl = PresenterImplementation(
            this,
            this
        )
        this.presenter = presenterImpl

        lifecycle.addObserver(presenterImpl)
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
    override fun showError(message: Int) {

        val errMsg = when (message){
            R.id.cheating_dead -> "Cheating death!"
            else -> "Undefined error"
        }

        Snackbar.make(binding.bar, errMsg, Snackbar.LENGTH_SHORT)
            .show()
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
