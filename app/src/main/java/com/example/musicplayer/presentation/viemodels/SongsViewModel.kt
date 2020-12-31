package com.example.musicplayer.presentation.viemodels

import android.content.ContentResolver
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.repository.SongRepository
import com.example.musicplayer.domain.SongInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SongsViewModel @ViewModelInject constructor(
    private val songRepository: SongRepository
): ViewModel() {
    var songlist = MutableLiveData<MutableList<SongInfo>>()
//  var currentPlayList = MutableLiveData<MutableList<SongInfo>>()

    fun getMusic(contentResolver: ContentResolver) {
        viewModelScope.launch(Dispatchers.IO) {
            songRepository.getAllMusicFromStorage(contentResolver)
        }

    }

}