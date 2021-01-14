package com.example.musicplayer.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.musicplayer.R
import com.example.musicplayer.data.adapter.SongViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_song_library.*


class SongLibraryFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songLibraryViewPagerInit()
    }

    private fun songLibraryViewPagerInit() {
        val viewPagerAdapter = SongViewPagerAdapter(childFragmentManager, lifecycle)
        viewPagerAdapter.apply {
            addFragment(AllSongFragment(), "All song")
            addFragment(RecentSongFragment(), "Recent")
            addFragment(FolderSongFragment(), "Folder")
            addFragment(FavoriteSongFragment(), "Favorite")
        }
        songViewPager.adapter = viewPagerAdapter
        songViewPager.isUserInputEnabled = true
        TabLayoutMediator(
            (song_page_tl as TabLayout),
            (songViewPager as ViewPager2)
        ) { tab, position ->
            tab.text = viewPagerAdapter.getName(position)
        }.attach()
    }

}