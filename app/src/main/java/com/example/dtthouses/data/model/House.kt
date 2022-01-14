package com.example.dtthouses.data.model

import android.graphics.Bitmap

data class House(
    val id: Int,
    val image: String,
    val price: Double,
    val bedrooms: Int,
    val bathrooms: Int,
    val size: Int,
    val description: String,
    val zip: String,
    val city: String,
    val latitude: Double,
    val longitude: Double,
    val createdDate: String,
    var locationDistance: Int = 0,
    private var bitmap: Bitmap
) {
    fun setBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
    }

    fun getBitmap(): Bitmap {
        return this.bitmap
    }
}
