package com.example.student.covidstats

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country_detail", primaryKeys = ["name", "id"])
class CountryDetailEntity (
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "confirmed") val confirmed: Int,
    @ColumnInfo(name = "deaths") val deaths: Int,
    @ColumnInfo(name = "recovered") val recovered: Int,
    @ColumnInfo(name = "active") val active: Int,
    @ColumnInfo(name = "new_recovered") val date: String
)