package com.example.student.covidstats

import androidx.lifecycle.LiveData

class Repository(private val countryEntityDao: CountryEntityDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allCountries: LiveData<List<CountryEntity>> = countryEntityDao.getCountriesOrdered()

    suspend fun insert(country: CountryEntity) {
        countryEntityDao.insert(country)
    }
    suspend fun update(country: CountryEntity) {
        countryEntityDao.updateCountry(country)
    }
}