package com.example.musicplayer.data.adapter

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.musicplayer.R

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, uri: Uri?) {
    Glide.with(view.context)
        .load(uri)
        .placeholder(R.drawable.ic_play)
        .into(view) }