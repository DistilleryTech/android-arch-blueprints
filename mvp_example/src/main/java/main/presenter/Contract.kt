package main.presenter

import com.distillery.android.domain.models.ToDoModel

public interface Contract{

    interface PublishToView{
        fun showToastMessage(message: String)
    }

    interface ForwardViewInteractionToPresenter{
        fun onClickCheckboxCompletion(item: ToDoModel, newState: Boolean)
        fun onClickDeleteTask(item: ToDoModel)
    }
}
