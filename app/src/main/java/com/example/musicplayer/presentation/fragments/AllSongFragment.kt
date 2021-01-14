package com.example.musicplayer.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.musicplayer.R
import com.example.musicplayer.data.adapter.MySongAdapter
import com.example.musicplayer.presentation.viemodels.SongsViewModel
import kotlinx.android.synthetic.main.fragment_playlist_song.*
import java.sql.Time
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class AllSongFragment : Fragment() {

    private val songsViewModel: SongsViewModel by activityViewModels()

    var mySongAdapter: MySongAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_song, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val value = songsViewModel.songlist.value
        songsViewModel.getRecentMusic()

        mySongAdapter =
            MySongAdapter(value!!) {
                songsViewModel.playOrToggleSong(it)
            }
        rv_song.apply {
            adapter = mySongAdapter
        }



    }

    fun getDate(milliSeconds: Long, dateFormat: String?): String? {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

}


