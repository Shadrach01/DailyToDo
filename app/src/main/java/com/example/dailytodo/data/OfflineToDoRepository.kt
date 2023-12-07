package com.example.dailytodo.data

import kotlinx.coroutines.flow.Flow

class OfflineToDoRepository(private val toDoDao: ToDoDao) : ToDoRepository {
    override suspend fun insertToDo(todo: ToDo) = toDoDao.insert(todo)

    override suspend fun updateToDo(todo: ToDo) = toDoDao.update(todo)

    override suspend fun deleteToDo(todo: ToDo) = toDoDao.delete(todo)

    override fun getToDo(id: Int): Flow<ToDo> = toDoDao.getToDO(id)

    override fun getAllToDo(): Flow<List<ToDo>> = toDoDao.getAllToDo()
}