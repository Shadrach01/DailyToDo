package com.example.dailytodo.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailytodo.data.ToDo
import com.example.dailytodo.data.ToDoRepository
import com.example.dailytodo.ui.screens.EditToDoDestination
import com.example.dailytodo.workManager.ToDoWorkManagerRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

class EditToDoViewModel(
    savedStateHandle: SavedStateHandle,
    private val toDoWorkManagerRepository: ToDoWorkManagerRepository,
    private val toDoRepository: ToDoRepository
) : ViewModel() {

    /**
     * Holds current to-do ui state
     * And it's gotten from the [AddNewToDoViewModel]
     */
    var toDoUiState by mutableStateOf(NewToDoUiState())
        private set

    private val todoId: Int = checkNotNull(savedStateHandle[EditToDoDestination.toDoIdArg])

    private fun validateInput(uiState: TodoDetails = toDoUiState.toDoDetails): Boolean {
        return with(uiState) {
            details.isNotBlank() && time.isNotBlank()
        }
    }

    init {
        viewModelScope.launch {
            toDoUiState = toDoRepository.getToDo(todoId)
                .filterNotNull()
                .first()
                .toToDoUiState(true)
        }
    }

    // Store the last scheduled notification time to avoid rescheduling
    private var lastScheduledNotificationTime: Long? = null

    fun updateUiState(todoDetails: TodoDetails) {
        toDoUiState =
            NewToDoUiState(
                toDoDetails = todoDetails,
                isEntryValid = validateInput(todoDetails)
            )
    }

    suspend fun updateTodo() {
        if (validateInput(toDoUiState.toDoDetails)) {
            toDoRepository.updateToDo(toDoUiState.toDoDetails.toItem())
        }
    }

    //Calculate the time
    private fun calculateTodoTmeMillis(toDo: ToDo): Long {
        val calendar = Calendar.getInstance()

        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, toDo.time.hour)
        calendar.set(Calendar.MINUTE, toDo.time.minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return calendar.timeInMillis
    }

    //Schedule the new notification time
    fun scheduleNotification(todo: TodoDetails) {
        // Calculate time difference between the current time and To-Do time
        val todoTimeMillis = calculateTodoTmeMillis(todo.toItem())

        if (todoTimeMillis > System.currentTimeMillis()) {
            //Only re-schedule the notification if the time has changed
            if (lastScheduledNotificationTime != todoTimeMillis) {
                toDoWorkManagerRepository.scheduleReminder(
                    todoTimeMillis,
                    TimeUnit.MILLISECONDS,
                    todo.details
                )
                lastScheduledNotificationTime = todoTimeMillis

            }
        }
    }


}