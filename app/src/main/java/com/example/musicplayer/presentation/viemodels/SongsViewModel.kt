package com.example.musicplayer.presentation.viemodels

import android.content.ContentResolver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.repository.SongRepository
import com.example.musicplayer.domain.SongInfo
import com.example.musicplayer.exoplayer.MusicServiceConnection
import com.example.musicplayer.exoplayer.isPlayEnabled
import com.example.musicplayer.exoplayer.isPlaying
import com.example.musicplayer.exoplayer.isPrepared
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val MEDIA_ROOT_ID = "root_id"

class SongsViewModel @ViewModelInject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    private val songRepository: SongRepository
): ViewModel() {

    var songlist = MutableLiveData<MutableList<SongInfo>>()
    val curPlayingSong = musicServiceConnection.curPlayingSong
    val playbackState = musicServiceConnection.playbackState
//  var currentPlayList = MutableLiveData<MutableList<SongInfo>>()

    fun getMusic(contentResolver: ContentResolver) {
        viewModelScope.launch(Dispatchers.IO) {
           val result = songRepository.getAllMusicFromStorage(contentResolver)

            songlist.postValue(songRepository.songList)
        }

    }

    fun skipToNextSong() {
        musicServiceConnection.transportControls.skipToNext()
    }

    fun skipToPreviousSong() {
        musicServiceConnection.transportControls.skipToPrevious()
    }

    fun seekTo(pos: Long) {
        musicServiceConnection.transportControls.seekTo(pos)
    }

    fun playOrToggleSong(mediaItem: SongInfo, toggle: Boolean = false) {
        val isPrepared = playbackState.value?.isPrepared ?: false
        if(isPrepared && mediaItem.mediaId.toString() ==
            curPlayingSong.value?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)) {
            playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> if(toggle) musicServiceConnection.transportControls.pause()
                    playbackState.isPlayEnabled -> musicServiceConnection.transportControls.play()
                    else -> Unit
                }
            }
        } else {
            musicServiceConnection.transportControls.playFromMediaId(mediaItem.mediaId.toString(), null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsubscribe(MEDIA_ROOT_ID, object : MediaBrowserCompat.SubscriptionCallback() {})
    }

}