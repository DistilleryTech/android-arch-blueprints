package main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.distillery.android.blueprints.mvp.todo.contract.TodoContract
import com.distillery.android.domain.models.ToDoModel
import com.distillery.android.ui.databinding.FragmentTodoBinding
import kotlinx.coroutines.InternalCoroutinesApi
import main.presenter.Presenter

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
                object : TodoListAdapter.CheckBoxInteraction {
                    override fun onClick(item: ToDoModel, newState: Boolean) =
                        presenter.onClickCheckboxCompletion(item)
                },
                object : TodoListAdapter.DeleteMarkInteraction {
                    override fun onclick(item: ToDoModel) {
                        presenter.onClickDeleteTask(item)
                    }
                }
            )
            adapter = recyclerPendingAdapter
        }
        binding.completedTodoList.apply {
            layoutManager = LinearLayoutManager(this@TodoFragment.context)
            recyclerDoneAdapter = TodoListAdapter(
                object : TodoListAdapter.CheckBoxInteraction {
                    override fun onClick(
                        item: ToDoModel,
                        newState: Boolean
                    ) = Unit
                },
                object : TodoListAdapter.DeleteMarkInteraction {
                    override fun onclick(item: ToDoModel) {
                        presenter.onClickDeleteTask(item)
                    }
                }
            )
            adapter = recyclerDoneAdapter
        }
        binding.buttonAdd.setOnClickListener {
            presenter.onClickAddTask()
        }
        val presenter = Presenter(
            recyclerPendingAdapter,
            recyclerDoneAdapter,
            this,this
        )
        this.presenter = presenter

        lifecycle.addObserver(presenter)
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
    override fun showError(message: String) { }

    @Suppress("EmptyFunctionBlock")
    override fun notifyTaskDeleted() { }

    @Suppress("EmptyFunctionBlock")
    override fun showPendingTasks(tasks: List<ToDoModel>) { }

    @Suppress("EmptyFunctionBlock")
    override fun showDoneTasks(tasks: List<ToDoModel>) { }
}
