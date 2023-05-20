package com.example.basenotificationapp.ui

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basenotificationapp.R
import com.example.basenotificationapp.data.NotificationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {

    private val _notificationState = MutableStateFlow<NotificationState?>(null)
    val notificationState: StateFlow<NotificationState?> = _notificationState.asStateFlow()

    fun applyState(state: NotificationState) {
        viewModelScope.launch {
            _notificationState.emit(state)
        }
    }

    fun reset() {
        viewModelScope.launch {
            _notificationState.emit(null)
        }
    }

    fun getNotification(applicationContext: Context): Notification =
        NotificationCompat.Builder(
            applicationContext,
            notificationState.value?.channel?.id ?: "channel_id"
        )
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(notificationState.value?.title)
            .setContentText(notificationState.value?.description)
            .setColor(getNotificationColor())
            .setColorized(true)
            .setPriority(
                notificationState.value?.priority?.code ?: NotificationManager.IMPORTANCE_DEFAULT
            )
            .build()

    fun clearState(){
        viewModelScope.launch {
            _notificationState.emit(null)
        }
    }

    private fun getNotificationColor() =
        notificationState.let {
            it.value?.channel?.colorId
        } ?: R.color.primary

}