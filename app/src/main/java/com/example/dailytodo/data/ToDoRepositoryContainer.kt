package com.example.dailytodo.data

import android.content.Context

/**
 * App container for Dependency injection.
 */

interface ToDoRepositoryContainer {
    val toDoRepository: ToDoRepository
}



/**
 * [ToDoRepositoryContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class ToDoRepositoryDataContainer(private val context: Context) : ToDoRepositoryContainer {
    /**
     * Implementation for [ToDoRepository]
     * */
    override val toDoRepository: ToDoRepository by lazy {
        OfflineToDoRepository(DailyToDoDataBase.getDataBase(context).todoDao())
    }
}