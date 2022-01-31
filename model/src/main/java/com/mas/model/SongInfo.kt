package com.mas.model

import android.net.Uri

data class SongInfo(
    var mediaId: Long? = null,
    var title: String? = null,
    var Author: String? = null,
    var songUrl: String? = null,
    var songImage: Uri? = null
)