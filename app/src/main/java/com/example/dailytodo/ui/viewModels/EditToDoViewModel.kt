package com.example.dailytodo.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailytodo.data.ToDoRepository
import com.example.dailytodo.ui.screens.EditToDoDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditToDoViewModel(
    savedStateHandle: SavedStateHandle,
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
}