package com.example.student.covidstats

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country")
class CountryEntity(
    @PrimaryKey @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "new_confirmed") val new_confirmed: Int,
    @ColumnInfo(name = "total_confirmed") val total_confirmed: Int,
    @ColumnInfo(name = "new_deaths") val new_deaths: Int,
    @ColumnInfo(name = "total_deaths") val total_deaths: Int,
    @ColumnInfo(name = "new_recovered") val new_recovered: Int,
    @ColumnInfo(name = "total_recovered") val total_recovered: Int


)