package com.example.dailytodo.workManager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.dailytodo.data.ToDo
import java.util.concurrent.TimeUnit

class WorkManagerToDoRepository(
    private val context: Context
) : ToDoWorkManagerRepository {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)


    override fun scheduleReminder(duration: Long, unit: TimeUnit, todoName: String) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", todoName)
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            duration,
            PendingIntent.getBroadcast(
                context,
                todoName.hashCode(),
                intent,
PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

            )
        )
    }

    override fun cancel(toDO: ToDo) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                toDO.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

            )
        )
    }
}

