package com.example.my_quran.worker

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.my_quran.QuranApplication.Companion.CHANNEL_ID
import com.example.my_quran.R
import com.example.my_quran.data.model.Resource
import com.example.my_quran.data.repository.QuranRepository
import com.example.my_quran.ui.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DailyQuranReminderWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val quranRepository: QuranRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val randomAyahResult = quranRepository.getRandomAyah()

            if (randomAyahResult is Resource.Success) {
                val ayah = randomAyahResult.data

                val intent = Intent(appContext, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }

                val pendingIntent = PendingIntent.getActivity(
                    appContext,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )

                val notificationManager = appContext
                    .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                val notification = NotificationCompat.Builder(appContext, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("Daily Quran Reminder")
                    .setContentText(ayah.translation)
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText("${ayah.text}\n\n${ayah.translation}\n\nSurah ${ayah.surahNumber}, Ayah ${ayah.numberInSurah}")
                    )
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()

                notificationManager.notify(NOTIFICATION_ID, notification)

                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        private const val NOTIFICATION_ID = 101
    }
}
