package com.example.musicplayer.data.adapter

import androidx.recyclerview.widget.AsyncListDiffer
import com.bumptech.glide.RequestManager
import com.example.musicplayer.R
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.song_card.view.*
import kotlinx.android.synthetic.main.song_card.view.song_title
import javax.inject.Inject

class AllSongAdapter @Inject constructor(
    private val glide: RequestManager
) : BaseSongAdapter(R.layout.song_card) {

    override val differ = AsyncListDiffer(this, diffCallback)

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.itemView.apply {
            song_title.text = song.title
            song_artist.text = song.Author
            glide.load(song.songImage).into(iv_music)

            setOnClickListener {
                onItemClickListener?.let { click ->
                    click(song)
                }
            }
        }
    }

}



















