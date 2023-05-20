package com.example.basenotificationapp.data

import com.example.basenotificationapp.R

enum class ChannelType(val id: String, val description: String, val colorId: Int) {

    GENERAL("channel_general", "General channel", R.color.notification_bg_general),
    GOSSIP("channel_gossip", "Gossip Channel", R.color.notification_bg_gossip),
    SPORTS("channel_sports", "Sports channel", R.color.notification_bg_sports),
    FINANCE("channel_finance", "Finance channel", R.color.notification_bg_finance),
}