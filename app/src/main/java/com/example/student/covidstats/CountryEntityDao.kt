package com.example.student.covidstats


import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface CountryEntityDao {

    @Query("SELECT * from country ORDER BY total_confirmed DESC")
    fun getCountriesOrdered(): LiveData<List<CountryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: CountryEntity)

    @Query("DELETE FROM country")
    suspend fun deleteAll()
    @Update
    suspend fun updateCountry(country: CountryEntity)

}