package com.mas.data.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.mas.model.SongInfo
import javax.inject.Inject

class SongRepository @Inject constructor() {

    var songList = mutableListOf<SongInfo>()
    var allSongs = mutableListOf<SongInfo>()

    @SuppressLint("Recycle", "Range")
    fun getAllMusicFromStorage(contentResolver: ContentResolver): MutableList<SongInfo> {

        val songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val songCursor: Cursor? = contentResolver.query(songUri, null, null, null, null)

        if (songCursor != null) {
            while (songCursor.moveToNext()) {
                val url =
                    songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val title =
                    songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                val author =
                    songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val mediaId =
                    songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID).toLong()
                Log.i("bang", "musicId: $mediaId")
                val image = getAlbumArt(mediaId)
                Log.i("bang", "musicImage: $image")
                songList.add(
                    SongInfo(
                        mediaId = mediaId,
                        title = title,
                        Author = author,
                        songUrl = url,
                        songImage = image
                    )
                )
            }
            allSongs.addAll(songList)
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
