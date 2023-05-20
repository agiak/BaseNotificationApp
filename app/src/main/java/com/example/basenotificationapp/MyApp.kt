package com.example.basenotificationapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree


@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            subscribeToNotificationsChannel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun subscribeToNotificationsChannel() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelsNames = resources.getStringArray(R.array.notification_channels_names)
        val channelsIds = resources.getStringArray(R.array.notification_channels_ids)
        val channelsDescriptions = resources.getStringArray(R.array.notification_channels_descriptions)

        val availableChannels = ArrayList<NotificationChannel>().apply {
            channelsIds.forEachIndexed { index, channelID ->
                val channel = NotificationChannel(
                    channelID,
                    channelsNames[index],
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = channelsDescriptions[index]
                }
                add(channel)
            }
        }
        manager.createNotificationChannels(availableChannels)
    }
}
