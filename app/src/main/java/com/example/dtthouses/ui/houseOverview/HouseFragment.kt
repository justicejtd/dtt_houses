package com.example.dtthouses.ui.houseOverview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dtthouses.R
import com.example.dtthouses.models.House

class HouseFragment : Fragment() {
    private val houses = ArrayList<House>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_house, container, false)
        setMockHouses()
        val rvHouses = view.findViewById<RecyclerView>(R.id.rvHouses)
        rvHouses.layoutManager = LinearLayoutManager(context)
        rvHouses.adapter = HouseAdapter(houses)
        return view
    }

    fun setMockHouses() {
        houses.add(House(0, "dsf", 0.0, 9, 0, 9, "sdf",
            "sdf", "sdf", 3, 3, ""))
        houses.add(House(0, "dsf", 0.0, 9, 0, 9, "sdf",
            "sdf", "sdf", 3, 3, ""))
        houses.add(House(0, "dsf", 0.0, 9, 0, 9, "sdf",
            "sdf", "sdf", 3, 3, ""))
        houses.add(House(0, "dsf", 0.0, 9, 0, 9, "sdf",
            "sdf", "sdf", 3, 3, ""))
        houses.add(House(0, "dsf", 0.0, 9, 0, 9, "sdf",
            "sdf", "sdf", 3, 3, ""))
    }
}