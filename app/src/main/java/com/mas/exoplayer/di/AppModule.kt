package com.mas.exoplayer.di

import android.content.Context
import com.mas.exoplayer.MusicServiceConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
object AppModule {

    @Singleton
    @Provides
    fun provideMusicServiceConnection(
        @ApplicationContext context: Context
    ) = MusicServiceConnection(context)
}