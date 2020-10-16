package com.distillery.android.blueprints.compose

import androidx.compose.foundation.Box
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.distillery.android.blueprints.compose.ui.*
import com.distillery.android.blueprints.mvvm.todo.viewmodel.TodoListViewModel
import com.distillery.android.domain.models.ToDoModel

@Composable
fun initScreen(viewModel: TodoListViewModel, onAddTodoClicked: () -> Unit) = landingPage {
    Scaffold(
            topBar = { topBar() },
            bodyContent = { todoListBodyContent(viewModel) },
            bottomBar = { bottomBar() },
            floatingActionButton = { floatingActionButton(onAddTodoClicked) },
            isFloatingActionButtonDocked = true,
            floatingActionButtonPosition = FabPosition.Center
    )
}

@Composable
private fun floatingActionButton(onAddTodoClicked: () -> Unit) = FloatingActionButton(
        onClick = { onAddTodoClicked() },
        shape = CircleShape
) {
    Icon(Icons.Filled.Add)
}


@Composable
private fun topBar() = TopAppBar(
        title = { Text(text = "Todo List", style = Styles.topBarTitle) }
)

@Composable
private fun bottomBar() = BottomAppBar(cutoutShape = CircleShape) {}

@Composable
private fun todoListBodyContent(vm: TodoListViewModel) = Stack {
    val todoList: State<List<ToDoModel?>> = vm.totalTodoListLiveData.observeAsState(emptyList())

    LazyColumnFor(
            items = todoList.value,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = InnerPadding(dp8)
    ) { todoItem ->
        if (todoItem == null) addLineSeparator()
        else addRowUI(todoItem, vm)
    }
    showSnackBar(vm)
}

@Composable
private fun addLineSeparator() = Box(
        modifier = Modifier.height(1.dp).fillMaxWidth(),
        shape = RectangleShape,
        backgroundColor = Color.Gray
)

@Composable
private fun addRowUI(
        todoItem: ToDoModel,
        vm: TodoListViewModel
) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (checkbox, title, description, icon) = createRefs()
        checkbox(todoItem, vm, checkbox)
        textTitle(todoItem, checkbox, title)
        textDescription(todoItem, title, description)
        iconClose(todoItem, vm, icon)
    }
}

@Composable
private fun ConstraintLayoutScope.checkbox(
        todoItem: ToDoModel,
        vm: TodoListViewModel,
        checkboxTag: ConstrainedLayoutReference
) = Checkbox(
        modifier = Modifier.constrainAs(checkboxTag) {
            start.linkTo(parent.start, margin = dp5)
            top.linkTo(parent.top, margin = dp5)
        },
        checked = todoItem.completedAt != null,
        onCheckedChange = {
            if (it) vm.completeTodo(todoItem)
        })

@Composable
private fun ConstraintLayoutScope.textTitle(
        todoItem: ToDoModel,
        checkboxTag: ConstrainedLayoutReference,
        titleTag: ConstrainedLayoutReference
) = Text(
        modifier = Modifier.constrainAs(titleTag) {
            start.linkTo(checkboxTag.end, margin = dp5)
        },
        text = todoItem.title
)

@Composable
private fun ConstraintLayoutScope.textDescription(
        todoItem: ToDoModel,
        titleTag: ConstrainedLayoutReference,
        descriptionTag: ConstrainedLayoutReference
) = Text(
        modifier = Modifier.constrainAs(descriptionTag) {
            start.linkTo(titleTag.start)
            top.linkTo(titleTag.bottom, margin = dp2)
        },
        text = todoItem.description
)

@Composable
private fun ConstraintLayoutScope.iconClose(
        todoItem: ToDoModel,
        vm: TodoListViewModel,
        iconTag: ConstrainedLayoutReference
) = IconButton(
        onClick = { vm.deleteTodo(todoItem) },
        modifier = Modifier.constrainAs(iconTag) { end.linkTo(parent.end) }
) {
    Icon(Icons.Filled.Close)
}