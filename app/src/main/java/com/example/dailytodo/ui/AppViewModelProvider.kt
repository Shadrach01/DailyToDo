package com.example.dailytodo.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dailytodo.DailyToDoApplication
import com.example.dailytodo.ui.viewModels.AddNewToDoViewModel
import com.example.dailytodo.ui.viewModels.EditToDoViewModel
import com.example.dailytodo.ui.viewModels.ToDoDetailsViewModel
import com.example.dailytodo.ui.viewModels.ToDoScreenViewModel


/**
 * Provides Factory to create instance of ViewModel for the entire DailyToDo app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {

        //Initializer for ToDoScreenViewModel
        initializer {
            ToDoScreenViewModel(
                dailyToDoApplication().sqlContainer.toDoRepository
            )
        }

        //Initializer for AddNewToDoViewModel
        initializer {
            AddNewToDoViewModel(
                dailyToDoApplication().workManagerContainer.toDoWorkManagerRepository,
                dailyToDoApplication().sqlContainer.toDoRepository
            )
        }

        //Initializer for ToDoDetailsViewModel
        initializer {
            (this[AndroidViewModelFactory.APPLICATION_KEY] as DailyToDoApplication)
            ToDoDetailsViewModel(
                this.createSavedStateHandle(),
                dailyToDoApplication().workManagerContainer.toDoWorkManagerRepository,
                dailyToDoApplication().sqlContainer.toDoRepository
            )
        }

        //Initializer for EditToDoScreen
        initializer {
            EditToDoViewModel(
                this.createSavedStateHandle(),
                dailyToDoApplication().sqlContainer.toDoRepository
            )
        }


        //Initializer for the
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [DailyToDoApplication].
 */
fun CreationExtras.dailyToDoApplication(): DailyToDoApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as DailyToDoApplication)