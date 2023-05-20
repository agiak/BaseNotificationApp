package com.example.basenotificationapp.data

data class NotificationState(
    val title: String,
    val description: String,
    val channel: ChannelType,
    val priority: PriorityType = PriorityType.DEFAULT
)
