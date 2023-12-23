package com.example.dailytodo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    navigateBack: () -> Unit,

    viewModel: AddNewToDoViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold { innerPadding ->
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
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.medium_padding))
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.medium_padding)),
        ) {
            Column(
                modifier = Modifier
                    .background(color = Color.Gray)
                    .height(400.dp)
            ) {
                NewTodoForm(
                    todo = newToDoUiState.toDoDetails,
                    onValueChange = onValueChange,
                    onTimeSelected = onTimeSelected
                )
                Row {
                    TextButton(
                        onClick = onSaveClicked,
                        enabled = newToDoUiState.isEntryValid,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.add))
                    }

                    TextButton(
                        onClick = onSaveClicked,
                        enabled = newToDoUiState.isEntryValid,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                }
            }
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
        TextField(
            value = todo.details,
            onValueChange = { onValueChange(todo.copy(details = it)) },
            label = { Text(stringResource(R.string.todo_text)) },
            placeholder = { Text(stringResource(R.string.input_details) ) },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                focusedContainerColor = Color.Transparent,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                unfocusedContainerColor = Color.Transparent,
                focusedPlaceholderColor = Color.White,
                unfocusedPlaceholderColor = Color.White,
                disabledPlaceholderColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,

            ),
            modifier = modifier
                .fillMaxWidth(),
            enabled = enabled,
            singleLine = false
        )
Spacer(modifier = Modifier.height(70.dp))
            TimePickerDialog(
                toDo = todo,
                onTimeSelected = { onTimeSelected(todo.copy(time = it)) }
            )
        Divider(color = Color.White, thickness = 1.dp)


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

    // Get the current time
    val currentTime = LocalTime.now()

    // Define a custom formatter for hours, minutes, and AM/PM
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")

    // Format the time using the custom formatter
    val formattedTime = currentTime.format(formatter)



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

    Row(
        modifier = modifier
            .padding()
            .clickable { timeState.show() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,

    ) {
        Icon(
            painter = painterResource(R.drawable.timer),
            contentDescription = "timer",
            tint = Color.White,
             modifier = Modifier.padding(
            start = dimensionResource(R.dimen.small_padding),
            end = dimensionResource(R.dimen.small_padding),
    )
    )
        Text(text = stringResource(R.string.select_time),
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            ),
            modifier = Modifier.padding(end = 6.dp))
        TextButton(
            onClick = {
                //Show the ClockDialog
                timeState.show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (selectedTime.value == null) {
                Text(text = formattedTime,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                )
            } else {
                Text(
                    text = buildAnnotatedString {
                        append(toDo.time)
                    },
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }

        }



        /**
         * If no time is selected, show the button
         * if the time has be set, show only the tme and do not show the button
         */
//        if (selectedTime.value == null) {
//            Button(
//                onClick = {
//                    //Show the ClockDialog
//                    timeState.show()
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(text = stringResource(id = R.string.select_time))
//            }
//        } else {
//            Text(
//                text = buildAnnotatedString {
//                    append("Time: ")
//                    append(toDo.time)
//
//                },
//                style = MaterialTheme.typography.titleLarge
//            )
//        }
    }
}




@Preview
@Composable
fun NewToDoFormPreview() {
    DailyToDoTheme {
        NewToDoBody(
            newToDoUiState = NewToDoUiState(
                TodoDetails(
                    details = "", time = "10:00 AM",
                )
            ),
            onSaveClicked = { /*TODO*/ },
            onValueChange = {},
            onTimeSelected = {}
        )
    }
}

