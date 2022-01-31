package com.mas.exoplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun NowPlayingScreen() {
    Column(
        modifier = Modifier.fillMaxWidth()
            .fillMaxHeight()
    ) {
        val songs = listOf(
            "banjd",
            "bkgsrjdd",
            "bkjczddd",
            "bkrtursdgjd",
            "bksdgjdd",
            "dgeeseg",
            "gssdgs",
        )

        PlayListItems(songs = songs)
    }
}

@Composable
fun PlayListItems(
    songs: List<String>
) {
    LazyRow {
        item(songs) {
            songs.forEach {
                PlayingSong(it)
            }
        }
    }
}

@Composable
fun PlayingSong(
    text: String
) {
    Box(
        modifier = Modifier
            .background(Color.Green)
    ) {
        Text(text = text)
    }
}
