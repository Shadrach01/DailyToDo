package com.example.dailytodo.workManager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.dailytodo.CHANNEL_ID
import com.example.dailytodo.MainActivity
import com.example.dailytodo.NOTIFICATION_ID
import com.example.dailytodo.NOTIFICATION_TITLE
import com.example.dailytodo.R
import com.example.dailytodo.REQUEST_CODE
import com.example.dailytodo.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
import com.example.dailytodo.VERBOSE_NOTIFICATION_CHANNEL_NAME


fun makeToDoReminderNotification(
    message: String,
    context: Context
) {

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        /** Create the NotificationChannel, but only on API 26+ because
         *the NotificationChannel class is new and not in the support library
         */
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(
            CHANNEL_ID,
            VERBOSE_NOTIFICATION_CHANNEL_NAME,
            importance
        )
        channel.description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION

        notificationManager.createNotificationChannel(channel)
    }

    val pendingIntent: PendingIntent = createPendingIntent(context)

    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.daily_todo_app_icon)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(
        NOTIFICATION_ID,
        notification
    )

}

private fun createPendingIntent(appContext: Context): PendingIntent {
    val intent = Intent(appContext, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    // Flag to detect unsafe launches of intents for Android 12 and higher
    // to improve platform security
    var flags = PendingIntent.FLAG_UPDATE_CURRENT

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        flags = flags or PendingIntent.FLAG_IMMUTABLE
    }

    return PendingIntent.getActivity(
        appContext,
        REQUEST_CODE,
        intent,
        flags
    )
}





