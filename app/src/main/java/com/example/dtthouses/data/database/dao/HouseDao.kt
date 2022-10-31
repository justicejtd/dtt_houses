package com.example.dtthouses.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dtthouses.data.model.House
import com.example.dtthouses.data.database.AppDatabase


/**
 * House dao interface.
 */
@Dao
interface HouseDao {
    /**
     * Gets list of [House] using "SELECT * FROM house" query to the [AppDatabase]
     */
    @Query("SELECT * FROM house")
    fun getAll(): List<House>

    /**
     * Save list of [House]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(houses: List<House>)
}