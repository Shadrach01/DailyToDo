package com.example.dailytodo.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dailytodo.R
import com.example.dailytodo.data.ToDo
import com.example.dailytodo.navigation.NavigationDestination
import com.example.dailytodo.ui.AppViewModelProvider
import com.example.dailytodo.ui.theme.DailyToDoTheme
import com.example.dailytodo.ui.viewModels.ToDoScreenViewModel
import com.example.dailytodo.ui.viewModels.formattedTime

/**
 * Composable to display the already selected ToDos
 */

// Navigation route for this screen
object ToDoScreensDestination : NavigationDestination {
    override val route = "todo"
    override val titleRes = R.string.app_name
}


@Composable
fun ToDoScreen(
    modifier: Modifier = Modifier,
    navigateToAddNewToDo: () -> Unit,
    navigateUp: () -> Unit,
    navigateToDoDoDetailsScreen: (Int) -> Unit,
    viewModel: ToDoScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),

) {
    val toDoUiState by viewModel.toDoUiState.collectAsState()


    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ToDoAppBar(
                canNavigateBack = true,
                title = stringResource(ToDoScreensDestination.titleRes),
                scrollBehavior = scrollBehavior,
                navigateUp = navigateUp
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAddNewToDo,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.large_padding))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        HomeBody(
            todoList = toDoUiState.todoList,
            onTodoClick = navigateToDoDoDetailsScreen,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}


@Composable
fun HomeBody(
    todoList: List<ToDo>,
    onTodoClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondary)
            .padding(dimensionResource(R.dimen.small_padding))
            .fillMaxSize()
    ) {

        // If no to-do has been added yet, display the text
        if (todoList.isEmpty()) {
            Text(
                text = stringResource(R.string.you_do_not_have_any_new_todo),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        } else {
            ToDoList(
                todoList = todoList,
                onTodoClick = { onTodoClick(it.id) }
            )
        }
    }
}


@Composable
fun ToDoList(
    todoList: List<ToDo>,
    onTodoClick: (ToDo) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(items = todoList, key = { it.id }) { todo ->
            ToDoItem(
                todo = todo,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.small_padding))
                    .clickable { onTodoClick(todo) }
            )
        }
    }
}


@Composable
private fun ToDoItem(
    modifier: Modifier = Modifier,
    notificationDone: Boolean = false,
    todo: ToDo
) {
    if (notificationDone) {
        Card(
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Box {
                Canvas(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    drawLine(
                        start = Offset(1000f, 300f),
                        end = Offset(0f, canvasHeight),
                        color = Color.White,
                        strokeWidth = 7f
                    )
                    drawLine(
                        start = Offset(0f, 266f),
                        end = Offset(canvasWidth, canvasHeight),
                        color = Color.White,
                        strokeWidth = 7f
                    )
                }

                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.medium_padding))

                ) {
                    Text(
                        text = todo.details,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Justify
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = formattedTime(todo),
                        style = MaterialTheme.typography.titleLarge
                    )

                }
            }
        }
    } else {
        Card(
            modifier = modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.medium_padding))

            ) {
                Text(
                    text = todo.details,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = formattedTime(todo),
                    style = MaterialTheme.typography.titleLarge
                )

            }
        }
    }
}


@Preview
@Composable
fun AddNewToDOPreview() {
    DailyToDoTheme {
        ToDoScreen(
            navigateToAddNewToDo = {},
            navigateToDoDoDetailsScreen = {},
            navigateUp = {}
        )
    }
}

@Preview
@Composable
fun HomeBodyListPreview() {
    DailyToDoTheme {
        HomeBody(
            todoList = listOf(
                ToDo(1, "PlayFootball", "0"), ToDo(2, "Sleep", "4"),
                ToDo(3, "Bath", "2")

            ), onTodoClick = {},
        )
    }
}


@Preview
@Composable
fun HomeBodyPreviewEmpty() {
    DailyToDoTheme {
        HomeBody(todoList = listOf(), onTodoClick = {} )
    }
}


@Preview
@Composable
fun ToDoItemPreview() {
    DailyToDoTheme {
        ToDoItem(todo = ToDo(1, "Play football", "12"), notificationDone = true)
    }
}