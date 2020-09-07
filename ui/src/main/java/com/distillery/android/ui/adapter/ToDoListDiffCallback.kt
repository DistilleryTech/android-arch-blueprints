package com.distillery.android.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.distillery.android.domain.models.ToDoModel

class ToDoListDiffCallback : DiffUtil.ItemCallback<ToDoModel>() {
    override fun areItemsTheSame(model: ToDoModel, otherModel: ToDoModel): Boolean {
        return model.uniqueId == otherModel.uniqueId
    }

    override fun areContentsTheSame(model: ToDoModel, otherModel: ToDoModel): Boolean {
        return model == otherModel
    }
}
