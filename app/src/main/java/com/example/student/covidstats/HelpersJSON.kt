package com.example.student.covidstats

class JsonResponse {
    var Global:Global? = null
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
    var Country: String? = null
    var CountryCode: String? = null
    var Province: String? = null
    var City: String? = null
    var CityCode: String? = null
    var Lat: String? = null
    var Lon: String? = null
    var Confirmed: Int? = null
    var Deaths: Int? = null
    var Recovered: Int? = null
    var Active: Int? = null
    var Date: String? = null
}
