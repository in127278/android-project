package com.example.student.covidstats

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class ApiResolver {
    companion object {
        private val apiUrlBase: String = "https://api.covid19api.com/"
//        private val apiUrlBase: String = "https://api.github.com/"
        private val userAgent: String =  "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:72.0) Gecko/20100101 Firefox/72.0"

        fun apiFetchAll(context: Context) {
            val apiEndpoint:String = "summary"
            val myConnection: HttpsURLConnection = URL(apiUrlBase+apiEndpoint).openConnection() as HttpsURLConnection
            myConnection.setRequestProperty("User-Agent", userAgent);
            if (myConnection.responseCode == 200) {
                Log.d("asd", "Success")
                try{
                    val responseBody: InputStream = myConnection.inputStream
                    val responseBodyReader = InputStreamReader(responseBody, "UTF-8")
                    var gson = Gson()
                    val json: JsonResponse = gson.fromJson(responseBodyReader, JsonResponse::class.java)
//                    var db  = CountryEntityRoomDatabase.getDatabase(context)
                    val countryDao = CountryEntityRoomDatabase.getDatabase(context).countryEntityDao()
                    var repository = Repository(countryDao)
                    for(item in json.Countries!!) {
                        var countryToInsert: CountryEntity = CountryEntity(
                            name = item.Country,
                            new_confirmed = item.NewConfirmed,
                            total_confirmed = item.TotalConfirmed,
                            new_deaths = item.NewDeaths,
                            total_deaths = item.TotalDeaths,
                            new_recovered = item.NewRecovered,
                            total_recovered = item.TotalRecovered
                            )
                        GlobalScope.launch {
                            repository.update(countryToInsert)
                        }





//                        db.countryEntityDao().insert(lol)
                    }
                } catch(e:IOException) {
                    e.printStackTrace()
                }
            } else {
                Log.d("asd", "Error status: " + myConnection.responseCode.toString())
            }
            myConnection.disconnect();
        }

        fun apiFetchDetails(country: String) {
            val apiEndpoint:String = "dayone/country/$country"
            val myConnection: HttpsURLConnection = URL(apiUrlBase+apiEndpoint).openConnection() as HttpsURLConnection
            myConnection.setRequestProperty("User-Agent", userAgent);
            if (myConnection.responseCode == 200) {
                Log.d("asd", "Success")
                try{
                    val responseBody: InputStream = myConnection.inputStream
                    val responseBodyReader = InputStreamReader(responseBody, "UTF-8")
                    var gson = Gson()

                    val founderListType: Type =
                        object : TypeToken<ArrayList<CountryDetailsData?>?>() {}.type
                    val json: List<CountryDetailsData> =
                        gson.fromJson(responseBodyReader, founderListType)

//                    val json: TypeToken<ArrayList<CountryDetails>> = gson.fromJson(responseBodyReader, List<CountryDetails>::class.java)
                    for(item in json) {
                        Log.d("asd", item.Country)
                    }
                } catch(e:IOException) {
                    e.printStackTrace()
                }
            } else {
                Log.d("asd", "Error status: " + myConnection.responseCode.toString())
            }
            myConnection.disconnect();
        }
    }

}
