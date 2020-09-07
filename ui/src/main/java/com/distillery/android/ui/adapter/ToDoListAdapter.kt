package com.distillery.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.distillery.android.domain.models.ToDoModel
import com.distillery.android.ui.adapter.ToDoListAdapter.ToDoViewHolder
import com.distillery.android.ui.databinding.ItemTodoBinding
import com.distillery.android.ui.strikeThrough

class ToDoListAdapter(
    private val onDeleteClickListener: (toDoModel: ToDoModel) -> Unit,
    private val onCompleteClickListener: (toDoModel: ToDoModel) -> Unit
) : ListAdapter<ToDoModel, ToDoViewHolder>(ToDoListDiffCallback()) {

    inner class ToDoViewHolder(private val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(toDoModel: ToDoModel) {
            binding.apply {
                titleTextView.text = toDoModel.title
                descriptionTextView.text = toDoModel.description
                deleteButton.setOnClickListener {
                    onDeleteClickListener(toDoModel)
                }
                completedCheckBox.isChecked = toDoModel.completedAt != null
                completedCheckBox.setOnClickListener {
                    if (completedCheckBox.isChecked) {
                        completedCheckBox.isChecked = false
                        onCompleteClickListener(toDoModel)
                    } else {
                        completedCheckBox.isChecked = true
                    }
                }
                if (toDoModel.completedAt != null) {
                    titleTextView.strikeThrough()
                    descriptionTextView.strikeThrough()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        return ToDoViewHolder(
                ItemTodoBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
