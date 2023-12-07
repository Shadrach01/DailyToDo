package com.example.dailytodo

import android.app.Application
import com.example.dailytodo.data.ToDoRepositoryContainer
import com.example.dailytodo.data.ToDoRepositoryDataContainer
import com.example.dailytodo.workManager.ToDoWorkContainer
import com.example.dailytodo.workManager.ToDoWorkRepositoryContainer

class DailyToDoApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var sqlContainer: ToDoRepositoryContainer
    lateinit var workManagerContainer: ToDoWorkContainer


    override fun onCreate() {
        super.onCreate()
        sqlContainer = ToDoRepositoryDataContainer(this)
        workManagerContainer = ToDoWorkRepositoryContainer(this)
    }

}

