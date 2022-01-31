package com.mas.exoplayer.callback

import android.util.Log
import android.widget.Toast
import com.example.musicplayer.service.MusicService
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import javax.inject.Inject

class MusicPlayerEventListener @Inject constructor(
    private val musicService: MusicService
) : Player.EventListener {

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        super.onPlayerStateChanged(playWhenReady, playbackState)
        if(playbackState == Player.STATE_READY && !playWhenReady) {
            musicService.stopForeground(false)
        }
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        super.onPlayerError(error)
        Toast.makeText(musicService, "An unknown error occured", Toast.LENGTH_LONG).show()
        Log.i("bang", "onPlayerError:  $error")
    }
}