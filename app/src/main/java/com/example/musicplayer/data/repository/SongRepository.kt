package com.example.musicplayer.data.repository

import android.content.ContentResolver
import android.database.Cursor
import android.media.MediaMetadata
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AlbumColumns.ALBUM_ART
import com.example.musicplayer.domain.SongInfo
import javax.inject.Inject

class SongRepository @Inject constructor() {

    var songList = mutableListOf<SongInfo>()
    var allSongs = mutableListOf<SongInfo>()

    fun getAllMusicFromStorage(contentResolver: ContentResolver): MutableList<SongInfo> {
        val songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val songCursor: Cursor? = contentResolver.query(songUri, null, null, null, null)

        if (songCursor != null) {
            while (songCursor!!.moveToNext()) {
                val url =
                    songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val title =
                    songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                val author =
                    songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val mediaId =
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
            allSongs.addAll(songList)
        }
        return allSongs
    }
}