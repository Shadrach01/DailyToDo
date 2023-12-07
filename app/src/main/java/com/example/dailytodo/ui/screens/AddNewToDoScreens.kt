package com.example.dailytodo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dailytodo.R
import com.example.dailytodo.navigation.NavigationDestination
import com.example.dailytodo.ui.AppViewModelProvider
import com.example.dailytodo.ui.theme.DailyToDoTheme
import com.example.dailytodo.ui.viewModels.AddNewToDoViewModel
import com.example.dailytodo.ui.viewModels.NewToDoUiState
import com.example.dailytodo.ui.viewModels.TodoDetails
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Composable to add new to-do
 */


// Navigation route for this screen
object NewTodoScreenDestination : NavigationDestination {
    override val route = "NewToDo"
    override val titleRes = R.string.add_new_todo
}



@Composable
fun NewToDoScreen(
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    navigateBack: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: AddNewToDoViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            ToDoAppBar(
                canNavigateBack = canNavigateBack,
                title = stringResource(NewTodoScreenDestination.titleRes),
                navigateUp = navigateUp
            )
        }
    ) { innerPadding ->
        NewToDoBody(
            newToDoUiState = viewModel.newToDoUiState,
            onSaveClicked = {
                coroutineScope.launch {
                    viewModel.saveItem()
                    navigateBack()
                }
            },
            onValueChange = viewModel::updateUiState,
            onTimeSelected = {
                viewModel.updateUiState(it)
                viewModel.scheduleNotification(it)


            },
            modifier = modifier.padding(innerPadding)
        )

    }
}




@Composable
fun NewToDoBody(
    modifier: Modifier = Modifier,
    newToDoUiState: NewToDoUiState,
    onSaveClicked: () -> Unit,
    onValueChange: (TodoDetails) -> Unit,
    onTimeSelected: (TodoDetails) -> Unit
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(dimensionResource(id = R.dimen.medium_padding))
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.medium_padding)),
    ) {
        NewTodoForm(
            todo = newToDoUiState.toDoDetails,
            onValueChange = onValueChange,
            onTimeSelected = onTimeSelected
        )
        Button(
            onClick = onSaveClicked,
            enabled = newToDoUiState.isEntryValid,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.save))
        }
    }
}

@Composable
fun NewTodoForm(
    modifier: Modifier = Modifier,
    todo: TodoDetails,
    onValueChange: (TodoDetails) -> Unit = {},
    enabled: Boolean = true,
    onTimeSelected: (TodoDetails) -> Unit

) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.medium_padding))
    ) {
        OutlinedTextField(
            value = todo.details,
            onValueChange = { onValueChange(todo.copy(details = it)) },
            placeholder = { Text(stringResource(R.string.input_details)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.outline,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            ),
            modifier = modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = false
        )
        TimePickerDialog(
            toDo = todo,
            onTimeSelected = { onTimeSelected(todo.copy(time = it)) }
        )
    }
}


@Composable
fun TimePickerDialog(
    modifier: Modifier = Modifier,
    toDo: TodoDetails,
    onTimeSelected: (String) -> Unit,
) {
    //Store the selected time using remember
    val selectedTime = remember {
        mutableStateOf<String?>(null)
    }
//    val selected = selectedTime.value!!.format(DateTimeFormatter.ofPattern("hh:mm a"))

    val timeState = rememberUseCaseState()

    //Clock Dialog Box
    ClockDialog(
        state = timeState,
        config = ClockConfig(
            is24HourFormat = false
        ),
        //Update the selectedTime when the user selects a time
        selection = ClockSelection.HoursMinutes { hours, minutes ->
            val newTime = LocalTime.of(hours, minutes)
            //Update the value directly
            selectedTime.value = newTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
            onTimeSelected(selectedTime.value ?: "")
        }
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        /**
         * If no time is selected, show the button
         * if the time has be set, show only the tme and do not show the button
         */
        if (selectedTime.value == null) {
            Button(
                onClick = {
                    //Show the ClockDialog
                    timeState.show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.select_time))
            }
        } else {
            Text(
                text = buildAnnotatedString {
                    append("Time: ")
                    append(toDo.time)

                },
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}


@Preview
@Composable
fun NewToDoScreenPreview() {
    DailyToDoTheme {
        NewToDoScreen(
            navigateBack = { /*TODO*/ },
            navigateUp = { /*TODO*/ },
        )
    }
}

@Preview
@Composable
fun NewToDoFormPreview() {
    DailyToDoTheme {
        NewToDoBody(
            newToDoUiState = NewToDoUiState(
                TodoDetails(
                    details = "I will wake up", time = "10:00"
                )
            ),
            onSaveClicked = { /*TODO*/ },
            onValueChange = {},
            onTimeSelected = {}
        )
    }
}

