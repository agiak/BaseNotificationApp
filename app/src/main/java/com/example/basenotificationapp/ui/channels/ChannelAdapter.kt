package com.example.basenotificationapp.ui.channels

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.basenotificationapp.data.Channel
import com.example.basenotificationapp.databinding.ItemChannelBinding

class ChannelAdapter(
    private var channels: List<Channel> = emptyList(),
) : RecyclerView.Adapter<ChannelAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(val binding: ItemChannelBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChannelBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            val channel = channels[position]
            with(channel) {
                binding.channelTitle.text = name
                binding.channelDescription.text = description
            }
        }
    }

    override fun getItemCount() = channels.size

    fun updateChannels(channels: List<Channel>) {
        this.channels = channels
        notifyDataSetChanged()
    }
}
