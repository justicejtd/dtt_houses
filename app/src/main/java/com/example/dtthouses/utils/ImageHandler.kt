package com.example.dtthouses.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Handles image from network using Glide.
 */
class ImageHandler {
    companion object {
        /**
         * Sets image to ImageView using Glide.
         * Default image will be used if there is error when trying to get an image from network
         */
        fun handleImage(
            imageUrl: String,
            context: Context,
            imageView: ImageView,
            defaultImage: Int
        ) {
            Glide.with(context)
                .load(imageUrl)
                .error(defaultImage)
                .into(imageView)
        }
    }
}