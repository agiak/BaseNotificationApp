package com.example.basenotificationapp.data

data class Channel(
    val id: String,
    val name: String,
    val description: String,
    val priorityType: PriorityType = PriorityType.DEFAULT
)