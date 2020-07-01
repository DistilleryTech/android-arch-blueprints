package main.view

import android.util.Log
import com.distillery.android.domain.models.ToDoModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.distillery.android.mvp_example.R
import com.distillery.android.ui.databinding.ItemTodoBinding
import kotlinx.android.extensions.LayoutContainer

@Suppress("VariableNaming")
class TodoListAdapter(
    private val interaction: Interaction?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DiffCallback = object : DiffUtil.ItemCallback<ToDoModel>() {
        override fun areItemsTheSame(oldItem: ToDoModel, newItem: ToDoModel): Boolean =
            oldItem.uniqueId == newItem.uniqueId

        override fun areContentsTheSame(oldItem: ToDoModel, newItem: ToDoModel): Boolean =
            oldItem == newItem
    }

    // A custom config and a ListUpdateCallback to dispatch updates to
    private val differ = AsyncListDiffer(
        ToDoRecyclerChangeCallback(this),
        AsyncDifferConfig.Builder(DiffCallback).build()
    )

    internal inner class ToDoRecyclerChangeCallback(
        private val adapter: TodoListAdapter
    ) : ListUpdateCallback {
        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            // to safely display items when moving position but without changes in the list
            adapter.notifyDataSetChanged()
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            // avoid weird scrolls when removing items from the list
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = ItemTodoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TodoViewHolder(
            itemBinding,
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TodoViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // In case there are changes on how to submit items to the list
    fun submitList(list: List<ToDoModel>) {
        differ.submitList(list)
    }

    class TodoViewHolder
    constructor(
        private val itemTodoBinding: ItemTodoBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemTodoBinding.root), LayoutContainer {
        override val containerView: View?
            get() = itemView

        fun bind(item: ToDoModel) = with(itemView) {
            itemTodoBinding.descriptionTextView.text = item.description
            itemTodoBinding.titleTextView.text = item.title
            itemTodoBinding.completedCheckBox.isChecked =
                item.completedAt != null
            itemTodoBinding.completedCheckBox.setOnClickListener {
                interaction?.onClickCheckBox(item, itemTodoBinding.completedCheckBox.isChecked)
            }
        }
    }

    interface Interaction{
        fun onClickCheckBox(item: ToDoModel, newState: Boolean)
    }
}
