package com.example.dailytodo.workManager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.dailytodo.data.ToDo
import java.util.concurrent.TimeUnit

class WorkManagerToDoRepository(
    private val context: Context
) : ToDoWorkManagerRepository {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)


    override fun scheduleReminder(duration: Long, unit: TimeUnit, todo: ToDo) {
        val todoName = todo.details

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", todoName)
        }

        val uniqueId = todo.id

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            duration,
            PendingIntent.getBroadcast(
                context,
                uniqueId.hashCode(),
                intent,
PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

            )
        )
    }

    override fun cancel(todo: ToDo) {
        val uniqueId = todo.id

        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                uniqueId.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

            )
        )
    }
}

