package com.example.musicplayer.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.musicplayer.R
import com.example.musicplayer.data.adapter.MySongAdapter
import com.example.musicplayer.presentation.viemodels.SongsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_select_song.*

@AndroidEntryPoint
class SelectSongFragment : Fragment() {

    // lateinit var songsViewModel: SongsViewModel
    private val songsViewModel: SongsViewModel by viewModels()

    var mySongAdapter: MySongAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_song, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //songsViewModel = ViewModelProvider(requireActivity()).get(SongsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val value = songsViewModel.songlist.value
        println("jalil 02 $value")


        mySongAdapter =
            MySongAdapter(value!!)
        rv_song.apply {
            adapter = mySongAdapter
        }


    }


}