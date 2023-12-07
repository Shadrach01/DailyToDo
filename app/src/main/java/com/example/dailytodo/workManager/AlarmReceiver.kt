package com.example.dailytodo.workManager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.dailytodo.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return

        showNotification(context!!, message)
    }

    private fun showNotification(context: Context, message: String) {
        val notificationMessage = context.getString(R.string.move_on, message)
        makeToDoReminderNotification(notificationMessage, context)
    }
}

