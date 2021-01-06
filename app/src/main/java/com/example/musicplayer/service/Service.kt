package com.example.musicplayer.service

import android.app.PendingIntent
import android.content.Intent
import android.media.browse.MediaBrowser
import android.media.session.MediaSession
import android.os.Build
import android.os.Bundle
import android.service.media.MediaBrowserService
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.RequiresApi
import com.example.musicplayer.exoplayer.MusicNotificationManager
import com.example.musicplayer.exoplayer.MusicSource
import com.example.musicplayer.exoplayer.callback.MusicPlaybackPreparer
import com.example.musicplayer.exoplayer.callback.MusicPlayerEventListener
import com.example.musicplayer.exoplayer.callback.MusicPlayerNotificationListener
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.properties.Delegates


private const val MEDIA_TAG = "MusicService"
private const val MEDIA_ROOT_ID = "root_id"
private const val NETWORK_ERROR = "NETWORK_ERROR"

@AndroidEntryPoint
class MusicService : MediaBrowserService() {

    @Inject
    lateinit var dataSourceFactory: DefaultDataSourceFactory

    @Inject
    lateinit var player: SimpleExoPlayer

    @Inject
    lateinit var musicSource: MusicSource

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var mediaSession: MediaSessionCompat

    private var mediaSessionConnector by Delegates.notNull<MediaSessionConnector>()

    private lateinit var musicNotificationManager: MusicNotificationManager

    var isForegroundService = false

    private var curPlayingSong: MediaMetadataCompat? = null

    private var isPlayerInitialized = false

    private lateinit var musicPlayerEventListener: MusicPlayerEventListener

    companion object {
        var curSongDuration = 0L
            private set
    }



    override fun onCreate() {
        super.onCreate()

        serviceScope.launch {
            musicSource.fetchMediaData(contentResolver)
        }

        val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it, 0)
        }

        mediaSession = MediaSessionCompat(this, MEDIA_TAG).apply {
            setSessionActivity(activityIntent)
            isActive = true
        }


        sessionToken = mediaSession.sessionToken.token as MediaSession.Token

        musicNotificationManager = MusicNotificationManager(
            this,
            mediaSession.sessionToken,
            MusicPlayerNotificationListener(this)
        ) {

        }


        val musicPlaybackPreparer = MusicPlaybackPreparer(musicSource) {
            curPlayingSong = it

            preparePlayer(
                musicSource.songs,
                it,
                true
            )

        }

        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlaybackPreparer(musicPlaybackPreparer)
        mediaSessionConnector.setQueueNavigator(MusicQueueNavigator())
        mediaSessionConnector.setPlayer(player)

        musicPlayerEventListener = MusicPlayerEventListener(this)
        player.addListener(MusicPlayerEventListener(this))
        musicNotificationManager.showNotification(player)


    }

    private inner class MusicQueueNavigator : TimelineQueueNavigator(mediaSession) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
            return musicSource.songs[windowIndex].description
        }
    }

    private fun preparePlayer(
        songs: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playNow: Boolean
    ) {
        val curSongIndex = if (curPlayingSong == null) 0 else songs.indexOf(itemToPlay)
        player.prepare(musicSource.asMediaSource(dataSourceFactory))
        player.seekTo(curSongIndex, 0L)
        player.playWhenReady = playNow
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        player.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        player.removeListener(musicPlayerEventListener)
        player.release()
    }


    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowser.MediaItem>>
    ) {
        when (parentId) {
            MEDIA_ROOT_ID -> {
                val resultsSent = musicSource.whenReady { isInitialized ->
                    if (isInitialized) {
                        result.sendResult(musicSource.asMediaItems() as MutableList<MediaBrowser.MediaItem>)
                        if (!isPlayerInitialized && musicSource.songs.isNotEmpty()) {
                            preparePlayer(
                                musicSource.songs,
                                musicSource.songs[0],
                                false
                            )
                            isPlayerInitialized = true
                        }
                    } else {
                        mediaSession.sendSessionEvent(NETWORK_ERROR, null)
                        result.sendResult(null)
                    }
                }
                if (!resultsSent) {
                    result.detach()
                }
            }
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(MEDIA_ROOT_ID, null)
    }
}


