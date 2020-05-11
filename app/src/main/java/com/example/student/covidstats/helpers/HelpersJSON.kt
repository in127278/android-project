package com.example.student.covidstats.helpers

class SummaryEndpointJSON {
    var Global: Global? = null
    var Countries:List<CountryData>? = null
    var Date:String? = null
}
class Global {
    var NewConfirmed: Int? = null
    var TotalConfirmed: Int? = null
    var NewDeaths: Int? = null
    var TotalDeaths: Int? = null
    var NewRecovered: Int? = null
    var TotalRecovered: Int? = null
}

class CountryData {
    lateinit var Country: String
    lateinit var CountryCode: String
    lateinit var Slug: String
    var NewConfirmed: Int = 0
    var TotalConfirmed: Int = 0
    var NewDeaths: Int = 0
    var TotalDeaths: Int = 0
    var NewRecovered: Int = 0
    var TotalRecovered: Int = 0
    lateinit var Date: String

}

class CountryDetailsData {
    lateinit var Country: String
    lateinit var CountryCode: String
    lateinit var Province: String
    lateinit var City: String
    lateinit var CityCode: String
    lateinit var Lat: String
    lateinit var Lon: String
    var Confirmed: Int = 0
    var Deaths: Int = 0
    var Recovered: Int = 0
    var Active: Int = 0
    lateinit var Date: String
}