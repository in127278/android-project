package com.example.student.covidstats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CountryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository
    val allCountries: LiveData<List<CountryEntity>>

    init {
        val countryDao = CountryEntityRoomDatabase.getDatabase(application).countryEntityDao()
        repository = Repository(countryDao)
        allCountries = repository.allCountries
    }

    fun insert(country: CountryEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(country)
    }
}