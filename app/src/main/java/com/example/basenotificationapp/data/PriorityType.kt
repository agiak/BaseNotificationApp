package com.example.basenotificationapp.data

import android.app.NotificationManager

enum class PriorityType(val code: Int, val description: String) {

    DEFAULT(NotificationManager.IMPORTANCE_DEFAULT, "Default importance"),
    HIGH(NotificationManager.IMPORTANCE_HIGH, "High importance"),
    LOW(NotificationManager.IMPORTANCE_LOW, "Low importance"),
}

fun Int.mapImportanceToPriorityType(): PriorityType =
    when (this) {
        NotificationManager.IMPORTANCE_DEFAULT -> PriorityType.DEFAULT
        NotificationManager.IMPORTANCE_HIGH -> PriorityType.HIGH
        NotificationManager.IMPORTANCE_LOW -> PriorityType.LOW
        else -> PriorityType.DEFAULT
    }