package com.example.dtthouses.models

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
    val latitude: Int,
    val longitude: Int,
    val createdDate: String
)
