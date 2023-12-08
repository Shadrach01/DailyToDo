package com.example.dailytodo.ui.viewModels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailytodo.data.ToDoRepository
import com.example.dailytodo.ui.screens.ToDoDetailsScreenDestination
import com.example.dailytodo.workManager.ToDoWorkManagerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve, update and delete an item from the [ToDoRepository]'s data source.
 */

class ToDoDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val toDoWorkManagerRepository: ToDoWorkManagerRepository,
    private val toDoRepository: ToDoRepository
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private val todoId: Int = checkNotNull(savedStateHandle[ToDoDetailsScreenDestination.toDoIdArg])

    val uiState: StateFlow<ToDoDetailsUiState> =
        toDoRepository.getToDo(todoId)
            .filterNotNull()
            .map {
                ToDoDetailsUiState(todoDetails = it.toToDoDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ToDoDetailsUiState()
            )

    suspend fun deleteItem() {
        toDoRepository.deleteToDo(uiState.value.todoDetails.toItem())
    }
val tag = "Cancel"

    fun cancelAlarm(todoDetails: TodoDetails) {
        toDoWorkManagerRepository.cancel(
            todoDetails.toItem()
        )
        Log.d(tag, "AlarmCancelled")
    }

}


/**
 * Ui state for the details screen
 * */
data class ToDoDetailsUiState(
    val todoDetails: TodoDetails = TodoDetails()
)