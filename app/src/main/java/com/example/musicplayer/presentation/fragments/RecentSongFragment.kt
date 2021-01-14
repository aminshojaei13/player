package com.example.musicplayer.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.musicplayer.R
import com.example.musicplayer.data.adapter.MySongAdapter
import com.example.musicplayer.data.adapter.RecentSongAdapter
import com.example.musicplayer.presentation.viemodels.SongsViewModel
import kotlinx.android.synthetic.main.fragment_playlist_song.*


class RecentSongFragment : Fragment() {

    private val songsViewModel: SongsViewModel by activityViewModels()

    var recentSongAdapter: RecentSongAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recent_song, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val value = songsViewModel.recent.value
        println("jalil 02 ${value?.size}")

        recentSongAdapter =
            RecentSongAdapter(value!!) {
                songsViewModel.playOrToggleSong(it)
            }
        rv_song.apply {
            adapter = recentSongAdapter
        }

    }


}