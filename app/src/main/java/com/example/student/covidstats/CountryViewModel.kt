package com.example.student.covidstats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations


class CountryViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository: Repository
    val allCountries: LiveData<List<CountryEntity>>
    val singleCountry: LiveData<CountryEntity>
    var countryDetail: LiveData<List<CountryDetailEntity>>
    private val mCountryName = MutableLiveData<String>()
    private val mCountryDao: CountryEntityDao = CountryEntityRoomDatabase.getDatabase(application).countryEntityDao()

    init {
        mRepository = Repository(mCountryDao)
        allCountries = mRepository.getAllCountries()
        singleCountry = Transformations.switchMap(mCountryName) { c -> mRepository.getSingleCountry(c)}
        countryDetail = Transformations.switchMap(mCountryName) {c -> mRepository.getCountryDetails(c)}
    }

    fun setCountryName(countryName: String) {
        this.mCountryName.value = countryName
    }
}