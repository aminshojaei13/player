package com.example.musicplayer.data.repository

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
    var songList2 = mutableListOf<SongInfo>()
    var allSongs = mutableListOf<SongInfo>()
    var allSongs2 = mutableListOf<SongInfo>()

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
                    songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
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

    fun getAllPlayFromStorage(contentResolver: ContentResolver): MutableList<SongInfo> {

        val songUri2 = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
//        Log.i("dang", "getAllPlayFromStorage: $songUri2")
        val songCursor2: Cursor? = contentResolver.query(
            songUri2,
            null,
            null,
            null, null
        )
//
//
        if (songCursor2 != null) {
            while (songCursor2.moveToNext()) {
                val url =
                    songCursor2.getString(songCursor2.getColumnIndex(MediaStore.Audio.Playlists.Members.DATA))
                val title =
                    songCursor2.getString(songCursor2.getColumnIndex(MediaStore.Audio.Playlists.Members.TITLE))
                val author =
                    songCursor2.getString(songCursor2.getColumnIndex(MediaStore.Audio.Playlists.Members.ARTIST))
                val mediaId =
                    songCursor2.getString(songCursor2.getColumnIndex(MediaStore.Audio.Playlists.Members._ID))
//                val albumId =
//                    songCursor2.getLong(songCursor2.getColumnIndex(MediaStore.Audio.Playlists.DISPLAY_NAME))
//                val image = getAlbumArt(albumId)
//                Log.i("bang", "albumId: $albumId")
                songList2.add(
                    SongInfo(
                        mediaId = mediaId,
                        albumId = null,
                        title = title,
                        Author = author,
                        songUrl = url,
                        songImage = null
                    )
                )
            }
            allSongs2.addAll(songList)
//            Log.i("bang22", "allsong2: $allSongs2")
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
