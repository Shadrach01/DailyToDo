package com.example.dailytodo.workManager

import com.example.dailytodo.data.ToDo
import java.util.concurrent.TimeUnit

interface ToDoWorkManagerRepository {
    fun scheduleReminder(duration: Long, unit: TimeUnit, todoName: String)
    fun cancel(toDO: ToDo)
}


