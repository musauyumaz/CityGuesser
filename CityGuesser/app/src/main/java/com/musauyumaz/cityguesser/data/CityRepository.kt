package com.musauyumaz.cityguesser.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class CityRepository(private val context : Context) {

    private var allCities : List<City> = emptyList()

    suspend fun initialize() = withContext(Dispatchers.IO){
        allCities = loadCitiesFromJson()
    }

    private fun loadCitiesFromJson() : List<City> {
        return try {
            val jsonString = context.assets
                .open("cities.json")
                .bufferedReader()
                .use { it.readText() }

            val gson = Gson()

            val listType = object : TypeToken<List<City>>(){}.type

            gson.fromJson(jsonString,listType)
        }catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }
    }

    fun getCitiesByDifficulty(difficulty: Difficulty) : List<City>{
        return allCities.filter { it.difficulty == difficulty }
    }

    fun getRandomCity(difficulty: Difficulty? = null): City?{
        val cities = if (difficulty != null){
            getCitiesByDifficulty(difficulty)
        }else{
            allCities
        }

        if(cities.isEmpty()) return null

        return cities.random()
    }

    fun getWrongOptions(correctCity: City, difficulty: Difficulty? = null): List<String>{

        val cities = if (difficulty != null){
            getCitiesByDifficulty(difficulty)
        }else{
            allCities
        }

        val wrongCities = cities.filter { it.name != correctCity.name }

        val availableCities = if(wrongCities.size < 3){
            allCities.filter { it.name!= correctCity.name }
        }else{
            wrongCities
        }

        return availableCities
            .shuffled()
            .take(3)
            .map { it.name }
    }

    fun generateOptions(correctCity: City, difficulty: Difficulty? = null): List<String>{
        val wrongOptions = getWrongOptions(correctCity, difficulty)

        val allOptions = wrongOptions + correctCity.name
        return allOptions.shuffled()
    }

    fun getTotalCityCount(): Int = allCities.size

    fun getCityCountByDifficulty(difficulty: Difficulty): Int{
        return allCities.count{it.difficulty == difficulty}
    }
}