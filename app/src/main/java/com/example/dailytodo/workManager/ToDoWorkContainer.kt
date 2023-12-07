package com.example.dailytodo.workManager

import android.content.Context

interface ToDoWorkContainer {
    val toDoWorkManagerRepository: ToDoWorkManagerRepository
}


class ToDoWorkRepositoryContainer(context: Context) : ToDoWorkContainer {
    override val toDoWorkManagerRepository = WorkManagerToDoRepository(context)
}

