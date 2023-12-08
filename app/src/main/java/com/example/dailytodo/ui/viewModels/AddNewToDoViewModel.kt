package com.example.dailytodo.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dailytodo.data.ToDo
import com.example.dailytodo.data.ToDoRepository
import com.example.dailytodo.workManager.ToDoWorkManagerRepository
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.concurrent.TimeUnit

class AddNewToDoViewModel(
    private val toDoWorkManagerRepository: ToDoWorkManagerRepository,
    private val toDoRepository: ToDoRepository,
) : ViewModel() {
    /**
     * Holds current to-do ui state
     */
    var newToDoUiState by mutableStateOf(NewToDoUiState())
        private set


    /**
     * Updates the [NewToDoUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(toDoDetails: TodoDetails) {
        newToDoUiState = NewToDoUiState(
            toDoDetails = toDoDetails, isEntryValid = validateInput(toDoDetails)
        )
    }


    // Verify that the screen is not blank
    private fun validateInput(uiState: TodoDetails = newToDoUiState.toDoDetails): Boolean {
        return with(uiState) {
            details.isNotBlank() && time.isNotBlank()
        }
    }


    suspend fun saveItem() {
        if (validateInput()) {
            toDoRepository.insertToDo(newToDoUiState.toDoDetails.toItem())
        }
    }

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


    fun scheduleNotification(todo: TodoDetails) {
        // Calculate time difference between the current time and the To-Do time
//        val currentTimeMillis = System.currentTimeMillis()
        val todoTimeMillis = calculateTodoTmeMillis(todo.toItem())

        if (todoTimeMillis > System.currentTimeMillis()) {
//            val timeDiffMillis = todoTimeMillis - currentTimeMillis

            toDoWorkManagerRepository.scheduleReminder(
                todoTimeMillis,
                TimeUnit.MILLISECONDS,
                todo.toItem()
            )

        }

    }

}




/**
 * UiState for a to-do
 */
data class NewToDoUiState(
    val toDoDetails: TodoDetails = TodoDetails(),
    val isEntryValid: Boolean = false,
    val notificationDone: Boolean = false
)

data class TodoDetails(
    val id: Int = 0,
    val details: String = "",
    val time: String = "",
)


/**
 * Extension function to convert the [TodoDetails] to [To-do].
 */
fun TodoDetails.toItem(): ToDo = ToDo(
    id = id,
    details = details,
    timeString = time
)


/**
 * Create a time converter function
 */
fun formattedTime(toDo: ToDo): String {
    return toDo.time.format(DateTimeFormatter.ofPattern("hh:mm a"))
}

/**
 * Extension function to convert [ToDo] to [NewToDoUiState]
 */
fun ToDo.toToDoUiState(isEntryValid: Boolean = false): NewToDoUiState = NewToDoUiState(
    toDoDetails = this.toToDoDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [To-Do] to [TodoDetails]
 */
fun ToDo.toToDoDetails(): TodoDetails = TodoDetails(
    id = id,
    details = details,
    time = timeString
)

