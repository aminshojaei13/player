package com.example.musicplayer.presentation.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.musicplayer.R
import com.example.musicplayer.presentation.viemodels.SongsViewModel
import com.example.musicplayer.service.MusicService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class HomeFragment : Fragment(), View.OnClickListener {

    var musicCurrentPosition: Int = 0




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

        songsViewModel.songlist.observe(viewLifecycleOwner, Observer {
            println("jalil 01 ${it.size}")
        })

        btn_paly_pause.setOnClickListener(this)
        btn_next.setOnClickListener(this)
        btn_previous.setOnClickListener(this)

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

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_paly_pause -> {
                requireActivity().startService(Intent(requireContext(), MusicService::class.java))
            }
            R.id.btn_next -> {

            }
            R.id.btn_previous -> {

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




}