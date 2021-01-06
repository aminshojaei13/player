package com.example.musicplayer.presentation.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.domain.SongInfo
import com.example.musicplayer.exoplayer.isPlaying
import com.example.musicplayer.exoplayer.toSong
import com.example.musicplayer.presentation.viemodels.SongsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment(), View.OnClickListener {

    private var curPlayingSong: SongInfo? = null

    private var playbackState: PlaybackStateCompat? = null

    private var shouldUpdateSeekbar = true

    private val songsViewModel: SongsViewModel by activityViewModels()

    companion object {
        private const val READ_STORAGE_PERMISSION_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObservers()

        songsViewModel.songlist.observe(viewLifecycleOwner) {
            println("jalil 01 ${it.size}")
        }

        songsViewModel.updateCurrentPlayerPosition()

        btn_paly_pause.setOnClickListener(this)
        btn_next.setOnClickListener(this)
        btn_previous.setOnClickListener(this)
        btn_seek_forward.setOnClickListener(this)
        btn_seek_back.setOnClickListener(this)

        play_list.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSelectSongFragment())
        }

        if (
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_STORAGE_PERMISSION_CODE
            )
        } else {
            songsViewModel.getMusic(requireActivity().contentResolver)
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    setCurPlayerTimeToTextView(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                shouldUpdateSeekbar = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    songsViewModel.seekTo(it.progress.toLong())
                    shouldUpdateSeekbar = true
                }
            }
        })

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_paly_pause -> {
                curPlayingSong?.let {
                    songsViewModel.playOrToggleSong(it, true)
//                    songsViewModel.updateCurrentPlayerPosition()
                }
            }
            R.id.btn_next -> {
                songsViewModel.skipToNextSong()
            }
            R.id.btn_previous -> {
                songsViewModel.skipToPreviousSong()
            }
            R.id.btn_seek_back -> {
                songsViewModel.seekTo(-5)
            }
            R.id.btn_seek_forward -> {
                songsViewModel.seekTo(5)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            songsViewModel.getMusic(requireActivity().contentResolver)
        }
    }

    private fun updateTitleAndSongImage(song: SongInfo) {
        val title = "${song.title} - ${song.Author}"
        song_title.text = title
        Glide.with(requireContext())
            .load(song.songImage)
            .placeholder(R.drawable.ic_play)
            .into(iv_music)
    }

    private fun subscribeToObservers() {
        songsViewModel.songlist.observe(viewLifecycleOwner) {
            it?.let { songs ->
                if (curPlayingSong == null && songs.isNotEmpty()) {
                    curPlayingSong = songs[0]
                    updateTitleAndSongImage(songs[0])
                }
            }
        }
        songsViewModel.curPlayingSong.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            curPlayingSong = it.toSong()
            updateTitleAndSongImage(curPlayingSong!!)
        }
        songsViewModel.playbackState.observe(viewLifecycleOwner) {
            playbackState = it
            btn_paly_pause.setBackgroundResource(
                if (playbackState?.isPlaying == true) R.drawable.ic_baseline_pause_24 else R.drawable.ic_baseline_play_arrow_24
            )
            seekBar.progress = it?.position?.toInt() ?: 0
        }
        songsViewModel.curPlayerPosition.observe(viewLifecycleOwner) {
            if (shouldUpdateSeekbar) {
                seekBar.progress = it.toInt()
                setCurPlayerTimeToTextView(it)
            }
        }
        songsViewModel.curSongDuration.observe(viewLifecycleOwner) {
            seekBar.max = it.toInt()
            val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
            tvSongDuration.text = dateFormat.format(it)
        }
    }

    private fun setCurPlayerTimeToTextView(ms: Long) {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        tvCurTime.text = dateFormat.format(ms)
    }

}