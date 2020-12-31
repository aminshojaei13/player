package com.example.musicplayer.data.repository

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import com.example.musicplayer.domain.SongInfo
import javax.inject.Inject

class SongRepository @Inject constructor(){

    var songList = mutableListOf<SongInfo>()

    suspend fun getAllMusicFromStorage(contentResolver: ContentResolver) {
        val songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val songCursor: Cursor? = contentResolver.query(songUri, null, null, null, null)

        if (songCursor != null) {
            while (songCursor!!.moveToNext()) {
                var url =
                    songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                var title =
                    songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                var author =
                    songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                var mediaId =
                    songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media._ID))

                songList.add(
                    SongInfo(
                        mediaId = mediaId,
                        title = title,
                        Author = author,
                        songUrl = url
                    )
                )
            }
        }
    }
}