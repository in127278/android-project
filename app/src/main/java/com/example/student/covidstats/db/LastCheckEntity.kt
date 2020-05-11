package com.example.student.covidstats.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "last_check")
class LastCheckEntity(
    @PrimaryKey @ColumnInfo(name = "check_type") val check_type: String,
    @ColumnInfo(name = "date") val date: String
)


