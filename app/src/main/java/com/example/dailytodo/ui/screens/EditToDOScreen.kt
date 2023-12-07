package com.example.dailytodo.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dailytodo.R
import com.example.dailytodo.navigation.NavigationDestination
import com.example.dailytodo.ui.AppViewModelProvider
import com.example.dailytodo.ui.theme.DailyToDoTheme
import com.example.dailytodo.ui.viewModels.EditToDoViewModel
import kotlinx.coroutines.launch

/**
 * Composable to edit the Todo
 * */

// Navigation route for this screen
object EditToDoDestination : NavigationDestination {
    override val route = "editScreen"
    override val titleRes = R.string.edit_todo_screen
    const val toDoIdArg = "toDoId"
    val routeWithArgs = "$route/{$toDoIdArg}"
}

@Composable
fun EditToDoScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    navigateBack: () -> Unit,
    viewModel: EditToDoViewModel = viewModel(factory = AppViewModelProvider.Factory)

) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            ToDoAppBar(
                canNavigateBack = true,
                title = stringResource(EditToDoDestination.titleRes),
                navigateUp = navigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        NewToDoBody(
            newToDoUiState = viewModel.toDoUiState,
            onSaveClicked = {
                coroutineScope.launch {
                    viewModel.updateTodo()
                    navigateBack()
                }
            },
            onValueChange = viewModel::updateUiState,
            onTimeSelected = viewModel::updateUiState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}


@Preview
@Composable
fun EditToDoScreenPreview() {
    DailyToDoTheme {
        EditToDoScreen(
            navigateUp = {},
            navigateBack = {}
        )
    }
}