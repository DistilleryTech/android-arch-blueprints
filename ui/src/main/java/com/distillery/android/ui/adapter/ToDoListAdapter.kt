package com.distillery.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.distillery.android.domain.models.ToDoModel
import com.distillery.android.ui.adapter.ToDoListAdapter.ToDoViewHolder
import com.distillery.android.ui.clicks.setDebounceClickListener
import com.distillery.android.ui.databinding.ItemTodoBinding
import com.distillery.android.ui.strikeThrough

class ToDoListAdapter(
    private val onDeleteClickListener: (toDoModel: ToDoModel) -> Unit,
    private val onCompleteClickListener: (toDoModel: ToDoModel) -> Unit
) : ListAdapter<ToDoModel, ToDoViewHolder>(ToDoListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        return ToDoViewHolder(ItemTodoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ToDoViewHolder(private val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(toDoModel: ToDoModel) {
            binding.apply {
                titleTextView.text = toDoModel.title
                descriptionTextView.text = toDoModel.description
                completedCheckBox.isChecked = toDoModel.isCompleted
                completedCheckBox.isEnabled = !toDoModel.isCompleted
                titleTextView.strikeThrough(show = toDoModel.isCompleted)
                descriptionTextView.strikeThrough(show = toDoModel.isCompleted)
                deleteButton.setDebounceClickListener {
                    onDeleteClickListener(toDoModel)
                }
                completedCheckBox.setDebounceClickListener {
                    completedCheckBox.isChecked = toDoModel.isCompleted
                    onCompleteClickListener(toDoModel)
                }
            }
        }
    }
}
