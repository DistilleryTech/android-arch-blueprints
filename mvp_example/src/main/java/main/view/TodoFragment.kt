package main.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.distillery.android.ui.adapter.ToDoListAdapter
import com.distillery.android.ui.databinding.FragmentTodoBinding
import kotlinx.coroutines.InternalCoroutinesApi
import main.presenter.Presenter
import java.lang.Exception

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TodoFragment : Fragment(){

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    // private lateinit var recyclerAdapter: ToDoListAdapter
    private lateinit var recyclerAdapter: TodoListAdapter

    private lateinit var presenter: Presenter

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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

        initRecyclerView()

        presenter.startFlow()

        binding.buttonAdd.setOnClickListener {
            //binding.progressBar.visibility= View.VISIBLE
            try {
                presenter.onCreateTodoClicked()
            }catch (e: Exception){
                Log.d(TAG, "TodoFragment : $e")
            }
        }
    }

    @InternalCoroutinesApi
    private fun initRecyclerView() {
        binding.todoList.apply {
            layoutManager = LinearLayoutManager(this@TodoFragment.context)
            recyclerAdapter = TodoListAdapter()
            adapter = recyclerAdapter
        }
        presenter = Presenter(recyclerAdapter, lifecycle)

        lifecycle.addObserver(presenter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // avoid memory leaks
        binding.todoList.adapter = null
        _binding = null
    }

    // ------------------------ CUSTOM STUFF

    private var param1: String? = null
    private var param2: String? = null

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TodoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TodoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        private val TAG = TodoFragment::class.qualifiedName ?: "TodoFragment"
    }
}
