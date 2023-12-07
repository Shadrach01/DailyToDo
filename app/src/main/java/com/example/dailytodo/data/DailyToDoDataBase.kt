package com.example.dailytodo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ToDo::class], version = 1, exportSchema = false)
abstract class DailyToDoDataBase: RoomDatabase() {

    abstract fun todoDao(): ToDoDao

    companion object {
        @Volatile
        private var Instance: DailyToDoDataBase? = null

        fun getDataBase(context: Context): DailyToDoDataBase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, DailyToDoDataBase::class.java, "todo_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}