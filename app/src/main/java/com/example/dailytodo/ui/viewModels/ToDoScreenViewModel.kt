package com.example.dailytodo.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailytodo.data.ToDo
import com.example.dailytodo.data.ToDoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ToDoScreenViewModel(
    toDoRepository: ToDoRepository
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val toDoUiState: StateFlow<ToDoUiState> =
        toDoRepository.getAllToDo().map { ToDoUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ToDoUiState()
            )


}


/**
 * Ui State for ToDOScreen
 */
data class ToDoUiState(
    val todoList: List<ToDo> = listOf(),
)
