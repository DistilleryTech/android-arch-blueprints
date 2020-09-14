package com.distillery.android.blueprints.compose

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.distillery.android.blueprints.compose.ui.Styles
import com.distillery.android.blueprints.compose.ui.dp100
import com.distillery.android.blueprints.compose.ui.dp5
import com.distillery.android.blueprints.compose.ui.landingPage
import com.distillery.android.blueprints.mvvm.todo.viewmodel.TodoListViewModel


@Composable
fun addTodoScreen(viewModel: TodoListViewModel, onSaveTodoClicked: () -> Unit) = landingPage {
    Scaffold(
        topBar = { topBar() },
        bodyContent = { addTodoBodyContent(viewModel) },
        bottomBar = { bottomBar() },
        floatingActionButton = { floatingActionButton(onSaveTodoClicked) },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center
    )
}

@Composable
private fun floatingActionButton(
    onSaveTodoClicked: () -> Unit
) = FloatingActionButton(
    onClick = {
        onSaveTodoClicked()
    },
    shape = CircleShape
) {
    Icon(Icons.Filled.Done)
}


@Composable
private fun topBar() = TopAppBar(
    title = { Text(text = "Add Todo", style = Styles.topBarTitle) }
)

@Composable
private fun bottomBar() = BottomAppBar(
    cutoutShape = CircleShape
) {}

@Composable
private fun addTodoBodyContent(vm: TodoListViewModel) = Column {
    addTitleField(vm)
    setDescriptionField(vm)
    showSnackBar(vm)
}

@Composable
private fun addTitleField(
    vm: TodoListViewModel
) {
    val titleState = vm.title.observeAsState("")
    TextField(
        value = titleState.value, onValueChange = { txt ->
            vm.onAddTitleChange(txt)
        }, label = {
            Text(text = "Title")
        },
        modifier = Modifier.padding(top = dp5).fillMaxWidth()
    )
}

@Composable
private fun setDescriptionField(vm: TodoListViewModel) {
    val descriptionState = vm.description.observeAsState("")
    TextField(
        value = descriptionState.value, onValueChange = { txt ->
            vm.onDescriptionChange(txt)
        }, label = {
            Text(text = "Description")
        },
        modifier = Modifier.padding(top = dp5).fillMaxWidth().preferredHeight(dp100)
    )
}