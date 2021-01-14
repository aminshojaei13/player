package com.example.musicplayer.exoplayer

import android.support.v4.media.MediaMetadataCompat
import com.example.musicplayer.domain.SongInfo

fun MediaMetadataCompat.toSong(): SongInfo? {
    return description?.let {
        SongInfo(
            it.mediaId,
            null,
            null,
            it.title.toString(),
            it.description.toString(),
            it.mediaUri.toString(),
            it.iconUri
        )
    }
}