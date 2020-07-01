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
import main.presenter.Presenter
import java.lang.Exception

class TodoFragment : Fragment(){

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
    @Suppress("TooGenericExceptionCaught")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPendingAndDoneRecyclerViews()

        binding.buttonAdd.setOnClickListener {
            // binding.progressBar.visibility= View.VISIBLE
            try {
                // presenter.onCreateTodoClicked()
                presenter.startFlow()
            }catch (e: Exception){
                Log.d(TAG, "TodoFragment : $e")
            }
        }
    }

    @InternalCoroutinesApi
    private fun initPendingAndDoneRecyclerViews() {
        binding.todoList.apply {
            layoutManager = LinearLayoutManager(this@TodoFragment.context)
            recyclerPendingAdapter = TodoListAdapter(
                object : TodoListAdapter.Interaction{
                    override fun onClickCheckBox(item: ToDoModel, newState: Boolean) =
                        presenter.onClickCheckBox(item, newState)
                }
            )
            adapter = recyclerPendingAdapter
        }
        binding.completedTodoList.apply {
            layoutManager = LinearLayoutManager(this@TodoFragment.context)
            recyclerDoneAdapter = TodoListAdapter(
                object : TodoListAdapter.Interaction{
                    override fun onClickCheckBox(item: ToDoModel, newState: Boolean) =
                        presenter.onClickCheckBox(item, newState)
                }
            )
            adapter = recyclerDoneAdapter
        }
        presenter = Presenter(recyclerPendingAdapter, recyclerDoneAdapter, lifecycle)

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
    }
}
