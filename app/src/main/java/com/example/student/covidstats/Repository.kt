package com.example.student.covidstats

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class Repository(private val countryEntityDao: CountryEntityDao) {

    private val apiUrlBase: String = "https://api.covid19api.com/"
    private val userAgent: String =  "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:72.0) Gecko/20100101 Firefox/72.0"
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
//    val allCountries: LiveData<List<CountryEntity>> = countryEntityDao.getCountriesOrdered()

    fun getAllCountries(): LiveData<List<CountryEntity>> {
        return countryEntityDao.getCountriesOrdered()
    }

    fun getSingleCountry(countryName: String): LiveData<CountryEntity> {
       return countryEntityDao.getSingleCountry(countryName)
    }

    fun getCountryDetails(countryName: String): LiveData<List<CountryDetailEntity>> {
        return countryEntityDao.getCountryDetail(countryName)
    }

    fun insertCountry(countries: List<CountryEntity>) {
        countryEntityDao.insertCountry(countries)
    }

    fun insertCountryDetail(detail: List<CountryDetailEntity>) {
        countryEntityDao.insertCountryDetail(detail)
    }


    fun apiFetchDetails(countryName: String) {
        //api is broken for usa
        if(countryName.toLowerCase() == "united states of america") { return }
        val apiEndpoint = "dayone/country/$countryName"
        val myConnection: HttpsURLConnection = URL(apiUrlBase+apiEndpoint).openConnection() as HttpsURLConnection
        myConnection.setRequestProperty("User-Agent", userAgent);
        if (myConnection.responseCode == 200) {
            try{
                val responseBody: InputStream = myConnection.inputStream
                val responseBodyReader = InputStreamReader(responseBody, "UTF-8")
                var gson = Gson()
                val founderListType: Type = object : TypeToken<ArrayList<CountryDetailsData?>?>() {}.type
                val json: List<CountryDetailsData> = gson.fromJson(responseBodyReader, founderListType)
//                val countryDao = CountryEntityRoomDatabase.getDatabase(context).countryEntityDao()
//                var repository = Repository(countryDao)
                var result = json.mapIndexed { index, daily -> CountryDetailEntity(
                    daily.Country,
                    index,
                    daily.Confirmed,
                    daily.Deaths,
                    daily.Recovered,
                    daily.Active,
                    daily.Date.substring(8,10) + "-"+ daily.Date.substring(5,7)
                ) }
                if(result.size < 180) {
//                        GlobalScope.launch {
                    var a = this.insertCountryDetail(result)
                    Log.d("asd", "Size: ${result.size}")
//                        }
                } else {
                    Log.d("asd", "Skipping details. Size exceeded: ${result.size}")
                }
            } catch(e: IOException) {
                e.printStackTrace()
            }
        } else {
            Log.d("asd", "Error status: " + myConnection.responseCode.toString())
        }
        myConnection.disconnect();
    }

    fun apiFetchAll() {
        val apiEndpoint = "summary"
        val myConnection: HttpsURLConnection = URL(apiUrlBase+apiEndpoint).openConnection() as HttpsURLConnection
        myConnection.setRequestProperty("User-Agent", userAgent);
        if (myConnection.responseCode == 200) {
            Log.d("asd", "Success")
            try{
                val responseBody: InputStream = myConnection.inputStream
                val responseBodyReader = InputStreamReader(responseBody, "UTF-8")
                var gson = Gson()
                val json: SummaryEndpointJSON = gson.fromJson(responseBodyReader, SummaryEndpointJSON::class.java)
                var result = json.Countries!!.map { item ->
                    CountryEntity(
                        name = item.Country,
                        new_confirmed = item.NewConfirmed,
                        total_confirmed = item.TotalConfirmed,
                        new_deaths = item.NewDeaths,
                        total_deaths = item.TotalDeaths,
                        new_recovered = item.NewRecovered,
                        total_recovered = item.TotalRecovered
                    )
                }
//                GlobalScope.launch {
                    this.insertCountry(result)
//                }
            } catch(e:IOException) {
                e.printStackTrace()
            }
        } else {
            Log.d("asd", "Error status: " + myConnection.responseCode.toString())
        }
        myConnection.disconnect();
    }

//    suspend fun update(country: CountryEntity) {
//        countryEntityDao.updateCountry(country)
//    }
}