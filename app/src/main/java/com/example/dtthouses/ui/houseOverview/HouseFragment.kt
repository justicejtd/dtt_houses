package com.example.dtthouses.ui.houseOverview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dtthouses.R
import com.example.dtthouses.base.ViewModelFactory
import com.example.dtthouses.data.api.ApiServiceImpl
import com.example.dtthouses.data.api.MainRepository
import com.example.dtthouses.data.model.House
import com.example.dtthouses.utils.Status

class HouseFragment : Fragment() {
    private val houseAdapter = HouseAdapter(arrayListOf())
    private lateinit var houseViewModel: HouseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_house, container, false)

        // Initialize views
        setupUI(view)

        // Setup view model
        val viewModelFactory = ViewModelFactory(MainRepository(ApiServiceImpl()))
        houseViewModel = ViewModelProvider(this, viewModelFactory)[HouseViewModel::class.java]

        // Setup observer
        houseViewModel.getHouses().observe(this as LifecycleOwner, {
            when(it.status) {
                Status.SUCCESS -> {
                    it.data?.let { houses -> houseAdapter.addHouses(houses) }
                }
                Status.LOADING -> {
                   // TODO show splash screen
                }
                else -> {}
            }
        })

        return view
    }

    private fun setupUI(view: View) {
        val rvHouses = view.findViewById<RecyclerView>(R.id.rvHouses)
        rvHouses.layoutManager = LinearLayoutManager(context)
        rvHouses.adapter = houseAdapter
    }
}