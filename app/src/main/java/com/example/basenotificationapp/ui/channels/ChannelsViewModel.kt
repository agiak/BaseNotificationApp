package com.example.basenotificationapp.ui.channels

import android.app.NotificationChannel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basenotificationapp.data.Channel
import com.example.basenotificationapp.data.mapImportanceToPriorityType
import com.example.basenotificationapp.domain.NotificationChannelManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChannelsViewModel @Inject constructor(
    private val notificationManager: NotificationChannelManager
) : ViewModel() {

    private val _channels = MutableStateFlow<List<Channel>>(emptyList())
    val channels: StateFlow<List<Channel>> = _channels.asStateFlow()

    init {
        getNotificationsChannels()
    }

    fun registerChannel(channel: Channel) = viewModelScope.launch {
        notificationManager.registerChannel(
            channel.id,
            channel.name,
            channel.description,
            channel.priorityType.code
        )
        getNotificationsChannels()
    }

    fun unregisterChannel(channelId: String) = viewModelScope.launch {
        notificationManager.unregisterChannel(channelId)
        getNotificationsChannels()
    }

    private fun getNotificationsChannels() {
        viewModelScope.launch {
            val availableChannels = notificationManager.getAllChannels()
            val appChannels = availableChannels.toAppChannelList()
            _channels.value = appChannels
        }
    }

    private fun List<NotificationChannel>.toAppChannelList(): List<Channel> {
        return ArrayList<Channel>().apply {
            this@toAppChannelList.forEachIndexed { index, notificationChannel ->
                if (index != 0) add(notificationChannel.toAppChannel())
            }
        }
    }

    private fun NotificationChannel.toAppChannel() = Channel(
        id = this.id,
        name = this.name.toString(),
        description = this.description ?: "",
        priorityType = this.importance.mapImportanceToPriorityType()
    )
}
