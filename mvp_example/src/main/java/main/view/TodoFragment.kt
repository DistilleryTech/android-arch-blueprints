package main.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.distillery.android.domain.models.ToDoModel
import com.distillery.android.ui.databinding.FragmentTodoBinding
import kotlinx.coroutines.InternalCoroutinesApi
import main.presenter.Contract
import main.presenter.Presenter

class TodoFragment : Fragment(),
    Contract.PublishToView,
    Contract.ForwardViewInteractionToPresenter
{

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerPendingAdapter: TodoListAdapter
    private lateinit var recyclerDoneAdapter: TodoListAdapter

    private lateinit var presenter: Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
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
                        //presenter.onClickCheckBox(item, newState)
                        onClickCheckboxCompletion(item, newState)
                },
                object : TodoListAdapter.DeleteMarkInteraction {
                    override fun onclick(item: ToDoModel) {
                        presenter.onClickDeleteMark(item)
                    }
                }
            )
            adapter = recyclerPendingAdapter
        }
        binding.completedTodoList.apply {
            layoutManager = LinearLayoutManager(this@TodoFragment.context)
            recyclerDoneAdapter = TodoListAdapter(
                object : TodoListAdapter.CheckBoxInteraction {
                    override fun onClick(item: ToDoModel, newState: Boolean) =
                        //presenter.onClickCheckBox(item, newState)
                        onClickCheckboxCompletion(item, newState)
                },
                object : TodoListAdapter.DeleteMarkInteraction {
                    override fun onclick(item: ToDoModel) {
                        presenter.onClickDeleteMark(item)
                    }
                }
            )
            adapter = recyclerDoneAdapter
        }
        presenter = Presenter(recyclerPendingAdapter, recyclerDoneAdapter, this)

        lifecycle.addObserver(presenter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // avoid memory leaks
        binding.todoList.adapter = null
        _binding = null
    }

    companion object {
        private val TAG = TodoFragment::class.qualifiedName ?: "TodoFragment"
        @JvmStatic
        fun newInstance() = TodoFragment()
    }

    @InternalCoroutinesApi
    override fun onClickCheckboxCompletion(item: ToDoModel, newState: Boolean) {
        presenter.onClickCheckboxCompletion(item, newState)
    }

    @Suppress("EmptyFunctionBlock")
    override fun showToastMessage(message: String) {}

    @Suppress("EmptyFunctionBlock")
    override fun onClickDeleteTask(item: ToDoModel) {}
}
