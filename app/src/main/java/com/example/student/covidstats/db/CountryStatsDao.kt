package com.example.student.covidstats.db


import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface CountryStatsDao {

    @Query("SELECT * FROM country ORDER BY total_confirmed DESC")
    fun getCountriesOrdered(): LiveData<List<CountryEntity>>

    @Query("SELECT country.name, new_confirmed, total_confirmed, new_deaths, total_deaths, new_recovered, total_recovered FROM country  JOIN country_continent ON country_continent.name = country.name WHERE country_continent.continent LIKE :continent ORDER BY total_confirmed DESC")
    fun getCountriesFiltered(continent: String): LiveData<List<CountryEntity>>

    @Query("SELECT COUNT(*) FROM country_continent")
    fun getAll(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountry(countries: List<CountryEntity>)

    @Query("SELECT * FROM country_detail WHERE name = :countryName ORDER BY id ASC")
    fun getCountryDetail(countryName: String): LiveData<List<CountryDetailEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountryDetail(details: Iterable<CountryDetailEntity>)

    @Query("SELECT * FROM country WHERE name = :countryName LIMIT 1")
    fun getSingleCountry(countryName: String): LiveData<CountryEntity>

    @Query("SELECT * FROM last_check WHERE check_type = :key LIMIT 1")
    fun getLastUpdate(key: String): LiveData<LastCheckEntity>

    @Query("SELECT * FROM last_check WHERE check_type = :key LIMIT 1")
    fun getLastCheck(key: String): LastCheckEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLastCheck(lastCheck: LastCheckEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContinent(continent: ContinentCountryEntity)
}