package com.example.student.covidstats


import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface CountryEntityDao {

    @Query("SELECT * FROM country ORDER BY total_confirmed DESC")
    fun getCountriesOrdered(): LiveData<List<CountryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountry(countries: List<CountryEntity>)

    @Query("SELECT * FROM country_detail WHERE name = :countryName ORDER BY id ASC")
    fun getCountryDetail(countryName: String): LiveData<List<CountryDetailEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountryDetail(details: Iterable<CountryDetailEntity>)

    @Query("SELECT * FROM country WHERE name = :countryName LIMIT 1")
    fun getSingleCountry(countryName: String): LiveData<CountryEntity>


}