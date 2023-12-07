package com.example.dailytodo.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [To-Do] from a given data source.
 */
interface ToDoRepository {
    /** Insert to-do to the data source*/
   suspend fun insertToDo(todo: ToDo)

    /** Update to-do in the data source*/
    suspend fun updateToDo(todo: ToDo)

    /** Delete to-do in the data source*/
    suspend fun deleteToDo(todo: ToDo)

    /** Retrieve a to-do from the given data source that matches with the id*/
    fun getToDo(id: Int): Flow<ToDo>

    /** Retrieve all to-do from the given data source*/
    fun getAllToDo(): Flow<List<ToDo>>
}