package com.example.musicplayer.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R
import com.example.musicplayer.databinding.SongCardBinding
import com.example.musicplayer.domain.SongInfo


class RecentSongAdapter(
    var myListSong: MutableList<SongInfo>,
    private val onSongClick: (song: SongInfo) -> Unit
) : RecyclerView.Adapter<RecentSongAdapter.MyViewHolder>() {

    class MyViewHolder(var binding: SongCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: SongCardBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.song_card, parent, false
        )

        return MyViewHolder(binding)
    }

    override fun getItemCount() = myListSong.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.song = myListSong[position]
        holder.itemView.setOnClickListener {
            onSongClick(myListSong[position])
        }
    }
}