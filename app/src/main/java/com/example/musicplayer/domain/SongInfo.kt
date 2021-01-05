package com.example.musicplayer.domain

import android.net.Uri

data class SongInfo(
    var mediaId: String? = null,
    var albumId: Long? = null,
    var title: String? = null,
    var Author: String? = null,
    var songUrl: String? = null,
    var songImage: Uri? = null

)