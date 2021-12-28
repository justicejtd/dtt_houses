package com.example.dtthouses.data.api

import com.example.dtthouses.data.model.House

interface ApiService {
    fun getHouses(): ArrayList<House>
}