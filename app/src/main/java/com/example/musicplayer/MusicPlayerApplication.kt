package com.example.musicplayer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MusicPlayerApplication : Application() {
    companion object {
        lateinit var publicApp: Application
    }

    override fun onCreate() {
        super.onCreate()
        publicApp = this
    }
}