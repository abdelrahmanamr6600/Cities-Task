package com.klivvr.cities.ui


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.klivvr.cities.databinding.ActivityMainBinding
import com.klivvr.cities.model.City
import com.klivvr.cities.model.CityItem

import com.klivvr.cities.viewmodel.MainActivityViewModel


class MainActivity : AppCompatActivity(),TextWatcher {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private val citiesAdapter =  CitiesAdapter()
    private  var citiesList = City()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        viewModel.readJsonFile()
        getCitiesList()
        setupEditTextSearch()
        getSearchResultList()

    }

    private fun setupRecyclerView(citiesList: List<CityItem>){

        citiesAdapter.diff.submitList(citiesList)
        binding.citiesRv.apply {
            adapter =  citiesAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        citiesAdapter.onCityCardClick={
            openGoogleMaps(it.lat,it.lon)
        }

    }
    private fun openGoogleMaps(latitude: Double, longitude: Double) {
        val gmmIntentUri = Uri.parse("geo:$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        }
    }
    private fun setupEditTextSearch(){
        binding.edSearch.addTextChangedListener(this)
    }

    private fun getCitiesList(){
        viewModel.getCities().observe(this) {
            citiesList = it
          setupRecyclerView(it)
        }
    }
    private fun search(list: List<CityItem>,prefix:String){
        viewModel.citiesSearch(list,prefix)
    }
    private fun getSearchResultList()
    {
        viewModel.observeSearchResultList().observe(this){
            if (it.isNotEmpty()){
                val adapter  = CitiesAdapter()
                adapter.diff.submitList(it)
                binding.citiesRv.adapter = adapter
            }
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
         if (binding.edSearch.text.isNotEmpty()){
             search(citiesList,binding.edSearch.text.toString())
         }
        else{
            setupRecyclerView(citiesList)
        }

    }
    override fun afterTextChanged(p0: Editable?) {
    }





}