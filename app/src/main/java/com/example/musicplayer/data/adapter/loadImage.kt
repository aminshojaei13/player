package com.example.musicplayer.data.adapter

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, uri: Uri?) {
    Glide.with(view.context)
        .load(uri)
        .into(view) }