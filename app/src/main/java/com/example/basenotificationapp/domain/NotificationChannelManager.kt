package com.example.basenotificationapp.domain

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class NotificationChannelManager(
    private val context: Context
) {

    private val notificationManager: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun getAllChannels(): List<NotificationChannel> = notificationManager.notificationChannels

    fun registerChannel(channelId: String, name: String, description: String, importance: Int) {
        val channel =
            NotificationChannel(channelId, name, importance).apply {
                this.description = description
            }
        notificationManager.createNotificationChannel(channel)
    }

    fun unregisterChannel(channelId: String) {
        notificationManager.deleteNotificationChannel(channelId)
    }
}
