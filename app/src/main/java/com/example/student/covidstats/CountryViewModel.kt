package com.example.student.covidstats

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations


class CountryViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository: Repository
    val allCountries: LiveData<List<CountryEntity>>
    val singleCountry: LiveData<CountryEntity>
    var countryDetail: LiveData<List<CountryDetailEntity>>
    var lastUpdate: LiveData<LastCheckEntity>
    private val mCountryName = MutableLiveData<String>()
    private val mFilterBy = MutableLiveData<String>()
    private val mCountryDao: CountryEntityDao = CountryEntityRoomDatabase.getDatabase(application).countryEntityDao()

    init {
        mRepository = Repository(mCountryDao)
//        allCountries = mRepository.getAllCountries()
        allCountries = Transformations.switchMap(mFilterBy) { c -> when(c) {
            PARAM_AFRICA -> {mRepository.getCountriesFiltered(c)}
            PARAM_ASIA -> {mRepository.getCountriesFiltered(c)}
            PARAM_EUROPE -> {mRepository.getCountriesFiltered(c)}
            PARAM_OCEANIA -> {mRepository.getCountriesFiltered(c)}
            PARAM_NORTH_AMERICA-> {mRepository.getCountriesFiltered(c)}
            PARAM_SOUTH_AMERICA -> {mRepository.getCountriesFiltered(c)}
            else -> {mRepository.getAllCountries()}
        }}
        singleCountry = Transformations.switchMap(mCountryName) { c -> mRepository.getSingleCountry(c)}
        countryDetail = Transformations.switchMap(mCountryName) {c -> mRepository.getCountryDetails(c)}
        lastUpdate = Transformations.switchMap(mCountryName) {c -> mRepository.getLastUpdate(c)}
    }

    fun setCountryName(countryName: String) {
        this.mCountryName.value = countryName
    }

    fun setFilterBy(filterBy: String) {
        this.mFilterBy.value = filterBy
    }
}