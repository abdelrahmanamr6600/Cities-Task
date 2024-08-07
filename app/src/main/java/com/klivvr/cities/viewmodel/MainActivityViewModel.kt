package com.klivvr.cities.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.klivvr.cities.Repository.CitiesRepository
import com.klivvr.cities.model.City
import com.klivvr.cities.model.CityItem

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private var citiesRepository  = CitiesRepository(application)

     fun readJsonFile(){
        citiesRepository.readJsonCitiesFromAssets()
    }
    fun getCities():MutableLiveData<City>{

        return citiesRepository.observeCitiesList()
    }



    fun citiesSearch(list: List<CityItem>,prefix:String){
        citiesRepository.searchCities(list,prefix)
    }
    fun observeSearchResultList():MutableLiveData<List<CityItem>>{
       return  citiesRepository.getSearchedList()
    }

}