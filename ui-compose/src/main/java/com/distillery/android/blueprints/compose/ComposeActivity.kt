package com.distillery.android.blueprints.compose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.platform.setContent
import com.distillery.android.blueprints.mvvm.todo.viewmodel.TodoListViewModel
import com.github.zsoltk.compose.backpress.AmbientBackPressHandler
import com.github.zsoltk.compose.backpress.BackPressHandler
import com.github.zsoltk.compose.router.Router
import org.koin.androidx.viewmodel.ext.android.viewModel

class ComposeActivity : AppCompatActivity() {
    private val viewModel by viewModel<TodoListViewModel>()
    private val backPressHandler = BackPressHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Providers(AmbientBackPressHandler provides backPressHandler) {
                galleryView(Routing.Home, viewModel)
            }
        }
    }

    override fun onBackPressed() {
        if (!backPressHandler.handle()) {
            super.onBackPressed()
        }
    }
}

@Composable
private fun galleryView(defaultRouting: Routing, viewModel: TodoListViewModel) {
    Router("GalleryView", defaultRouting) { backStack ->
        when (backStack.last()) {
            is Routing.Home -> initScreen(viewModel) {
                backStack.push(Routing.AddTodo)
            }
            is Routing.AddTodo -> addTodoScreen(viewModel) {
                viewModel.addTodo()
                backStack.pop()
            }
        }
    }
}

@Composable
fun showSnackBar(vm: TodoListViewModel) {
//todo implement snackbar
}

private sealed class Routing {
    object Home : Routing()
    object AddTodo : Routing()
}