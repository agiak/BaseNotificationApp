package com.example.basenotificationapp.di

import android.content.Context
import com.example.basenotificationapp.domain.NotificationChannelManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ChannelsModule {

   @Provides
   fun provideChannelManager(@ApplicationContext context: Context) = NotificationChannelManager(context)
}