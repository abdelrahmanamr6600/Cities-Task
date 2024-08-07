package com.klivvr.cities.Repository
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.klivvr.cities.model.City
import com.klivvr.cities.model.CityItem
import java.util.Locale

class CitiesRepository(private var application: Application) {

    private val citiesList: MutableLiveData<City> = MutableLiveData()
    private val searchResultList: MutableLiveData<List<CityItem>> = MutableLiveData()
    private fun convertCitiesJsonToModel(jsonString: String): City {
        return Gson().fromJson(jsonString, object : TypeToken<City>() {}.type)
    }

    /********************************Read Json Data*****************************/

    fun readJsonCitiesFromAssets() {
        citiesList.value = convertCitiesJsonToModel(application.assets.open("cities.json")
            .bufferedReader().use { it.readText() })
    }

    fun observeCitiesList(): MutableLiveData<City> {
        // First sort by city name
        quickCitiesSort(citiesList.value!!, 0, citiesList.value!!.size - 1) { a, b -> a.name < b.name }
        // Then sort by country, preserving the city name order within the same country
        quickCitiesSort(citiesList.value!!, 0, citiesList.value!!.size - 1) { a, b ->
            if (a.country == b.country) a.name < b.name else a.country < b.country
        }
        return citiesList
    }

  // Quick Sort Algorithm For sort Cities Alphabetical
    private fun quickCitiesSort(arr: MutableList<CityItem>, low: Int, high: Int, compare: (CityItem, CityItem) -> Boolean): MutableList<CityItem> {
        if (low < high) {
            val pi = partition(arr, low, high, compare)
            quickCitiesSort(arr, low, pi - 1, compare)
            quickCitiesSort(arr, pi + 1, high, compare)
        }
        return arr
    }

    private fun partition(arr: MutableList<CityItem>, low: Int, high: Int, compare: (CityItem, CityItem) -> Boolean): Int {
        val pivot = arr[high]
        var i = low - 1
        for (j in low until high) {
            if (compare(arr[j], pivot)) {
                i++
                arr.swap(i, j)
            }
        }
        arr.swap(i + 1, high)
        return i + 1
    }

    fun MutableList<CityItem>.swap(i: Int, j: Int) {
        val temp = this[i]
        this[i] = this[j]
        this[j] = temp
    }


    /********************************search*****************************/

    fun searchCities(cities: List<CityItem>, prefix: String)   {
        searchResultList.value =  binarySearch(cities, prefix)
    }
    fun binarySearch(cities: List<CityItem>, prefix: String): List<CityItem> {
        val result = mutableListOf<CityItem>()
        val lowerCasePrefix = prefix.lowercase(Locale.ROOT)
        var low = 0
        var high = cities.size - 1

        // Find the first occurrence of a city starting with the prefix (case-insensitive)
        while (low <= high) {
            val mid = (low + high) / 2
            val city = cities[mid]

            when {
                city.name.lowercase(Locale.ROOT).startsWith(lowerCasePrefix) -> {
                    // Find all cities starting with the prefix (case-insensitive)
                    var start = mid
                    while (start >= 0 && cities[start].name.lowercase(Locale.ROOT).startsWith(lowerCasePrefix)) {
                        start--
                    }
                    start++

                    var end = mid
                    while (end < cities.size && cities[end].name.lowercase(Locale.ROOT).startsWith(lowerCasePrefix)) {
                        end++
                    }

                    for (i in start until end) {
                        result.add(cities[i])
                    }
                    break
                }
                city.name.lowercase(Locale.ROOT) < lowerCasePrefix -> low = mid + 1
                else -> high = mid - 1
            }
        }
        return result
    }


    fun getSearchedList():MutableLiveData<List<CityItem>>{
        return searchResultList
    }



}


