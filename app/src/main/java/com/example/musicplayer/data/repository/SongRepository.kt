package com.example.musicplayer.data.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.musicplayer.domain.SongInfo
import javax.inject.Inject

class SongRepository @Inject constructor() {

    var songList = mutableListOf<SongInfo>()
    var allSongs = mutableListOf<SongInfo>()

    @SuppressLint("Recycle")
    fun getAllMusicFromStorage(contentResolver: ContentResolver): MutableList<SongInfo> {

        val songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val songCursor: Cursor? = contentResolver.query(
            songUri,
            null,
            MediaStore.Audio.Media.IS_MUSIC + " = 1",
            null, null
        )


        if (songCursor != null) {
            while (songCursor.moveToNext()) {
                val url =
                    songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val title =
                    songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                val author =
                    songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val mediaId =
                    songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media._ID))
                val albumId =
                    songCursor.getLong(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                val image = getAlbumArt(albumId)
                Log.i("bang", "albumId: $albumId")
                songList.add(
                    SongInfo(
                        mediaId = mediaId,
                        albumId = albumId,
                        title = title,
                        Author = author,
                        songUrl = url,
                        songImage = image
                    )
                )
            }
            allSongs.addAll(songList)
//            Log.i("bang", "musicId: $allSongs")
        }
        return allSongs
    }

    private fun getAlbumArt(albumId:Long): Uri? {
        return try {
            val genericArtUri = Uri.parse("content://media/external/audio/albumart")
            ContentUris.withAppendedId(genericArtUri, albumId)
        } catch (e: Exception) {
            null
        }
    }
}
