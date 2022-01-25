package com.example.dtthouses.data.model

/**
 * House model data class.
 */
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
    /**
     * Keeps track of the location distance between current user and other other house(s).
     * The default value is 0.
     */
    var locationDistance: Int = 0,
)

