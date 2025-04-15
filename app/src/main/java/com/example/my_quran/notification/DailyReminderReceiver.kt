package com.example.my_quran.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.my_quran.worker.DailyQuranReminderWorker

class DailyReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val workRequest = OneTimeWorkRequestBuilder<DailyQuranReminderWorker>()
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
}
