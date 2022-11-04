package com.example.dtthouses.data.database.dao

import androidx.room.*
import com.example.dtthouses.data.model.House
import com.example.dtthouses.data.database.AppDatabase


/**
 * House dao interface.
 */
@Dao
interface HouseDao {
    /**
     * Gets list of [House] using "SELECT * FROM house" query to the [AppDatabase].
     */
    @Query("SELECT * FROM house")
    fun getAll(): List<House>

    /**
     * Gets house where id equal the inputted id.
     */
    @Query("SELECT * FROM house WHERE id IN (:id)")
    fun findById(id: Int): House

    /**
     * Save list of [House].
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(houses: List<House>)

    /**
     * Gets house where search query is like city and/or zip code.
     */
    @Query("SELECT * FROM house WHERE REPLACE(UPPER(city || zip), ' ', '') " +
            "LIKE '%' || REPLACE((:searchQuery), ' ', '') || '%' " +
            "OR REPLACE(UPPER(zip || city), ' ', '') " +
            "LIKE '%' || REPLACE(UPPER(:searchQuery), ' ', '') || '%'")
    fun findBySearchQuery(searchQuery: String): List<House>

    /**
     * Update all houses data.
     */
    @Update
    fun updateHouses(houses: List<House>)
}