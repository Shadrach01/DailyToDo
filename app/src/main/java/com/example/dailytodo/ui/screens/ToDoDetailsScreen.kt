package com.example.dailytodo.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dailytodo.R
import com.example.dailytodo.data.ToDo
import com.example.dailytodo.navigation.NavigationDestination
import com.example.dailytodo.ui.AppViewModelProvider
import com.example.dailytodo.ui.theme.DailyToDoTheme
import com.example.dailytodo.ui.viewModels.ToDoDetailsUiState
import com.example.dailytodo.ui.viewModels.ToDoDetailsViewModel
import com.example.dailytodo.ui.viewModels.TodoDetails
import com.example.dailytodo.ui.viewModels.toItem
import kotlinx.coroutines.launch

/**
 * Composable to display details of the to-do
 * and a button to delete the to-do
 */

// Navigation route for this screen
object ToDoDetailsScreenDestination : NavigationDestination {
    override val route = "detailsScreen"
    override val titleRes = R.string.todo_screen
    const val toDoIdArg = "toDoId"
    val routeWithArgs = "$route/{$toDoIdArg}"
}

@Composable
fun ToDoDetailsScreen(
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    navigateBack: () -> Unit,
    navigateToEditTodo: (Int) -> Unit,
    viewModel: ToDoDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {


    val uiState = viewModel.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            ToDoAppBar(
                canNavigateBack = canNavigateBack,
                title = stringResource(ToDoDetailsScreenDestination.titleRes),
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditTodo(uiState.value.todoDetails.id) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.large_padding))
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit)
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        ToDoDetailsBody(
            toDoDetailsUiState = uiState.value,
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteItem()
                }
                navigateBack()
            },
            cancelAlarm = {
                viewModel.cancelAlarm(it)
            },
            todo = uiState.value.todoDetails.toItem(),
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun ToDoDetailsBody(
    toDoDetailsUiState: ToDoDetailsUiState,
    onDelete: () -> Unit,
    todo: ToDo,
    cancelAlarm: (ToDo) -> Unit,
    modifier: Modifier = Modifier,
) {
    var deleteConfirmationRequired by rememberSaveable {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.medium_padding)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.medium_padding))

    ) {
        ToDoDetails(
            todo = toDoDetailsUiState.todoDetails.toItem(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.delete))
        }

        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                    cancelAlarm(todo)
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.medium_padding))

            )
        }

    }
}


@Composable
fun ToDoDetails(
    modifier: Modifier = Modifier,
    todo: ToDo
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.medium_padding)),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.medium_padding)
            )
        ) {
            ToDoDetailRow(
                detailLabelRes = R.string.todo_details,
                toDo = todo,
                timeLabelRes = R.string.time
            )

        }
    }
}


@Composable
fun ToDoDetailRow(
    modifier: Modifier = Modifier,
    @StringRes detailLabelRes: Int,
    @StringRes timeLabelRes: Int,
    toDo: ToDo,

    ) {
    Column(
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.medium_padding))
    ) {
        Text(
            text = stringResource(detailLabelRes),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = toDo.details,
            style = MaterialTheme.typography.titleMedium,

            )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.large_padding)))
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onPrimaryContainer)
                .padding(dimensionResource(id = R.dimen.large_padding)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.large_padding))
        ) {
            Text(
                text = stringResource(timeLabelRes),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = toDo.timeString,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary

            )
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    modifier: Modifier = Modifier,
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { /*Do nothing */ },
        title = { Text(text = stringResource(id = R.string.attention)) },
        text = { Text(text = stringResource(R.string.want_to_delete)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(id = R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(id = R.string.yes))
            }
        }
    )
}

//@Preview
//@Composable
//fun ToDoDetailsPreview() {
//    DailyToDoTheme {
//        ToDoDetailsBody(
//            ToDoDetailsUiState(
//                todoDetails = TodoDetails(1, "WakeUp", "2")
//            ),
//            onDelete = {},
//            cancelAlarm = {},
//           todo = TodoDetails
//        )
//    }
//}