package com.example.dailytodo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Interface for the Data Access Object
 * */

@Dao
interface ToDoDao {
    //Insert the new to-do into the database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(todo: ToDo)

    //Update the selected to-do
    @Update
    suspend fun update(todo: ToDo)

    //Delete to-do
    @Delete
    suspend fun delete(todo: ToDo)

    //Call a particular to-do by id
    @Query("SELECT * from todos WHERE id = :id")
    fun getToDO(id: Int): Flow<ToDo>

    //Call all the to-dos
    @Query("SELECT * from todos ORDER BY id ASC")
    fun getAllToDo(): Flow<List<ToDo>>
}