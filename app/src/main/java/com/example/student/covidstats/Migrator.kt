package com.example.student.covidstats

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migrator {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "CREATE TABLE 'last_check' ('check_type' TEXT NOT NULL, 'date' TEXT NOT NULL, PRIMARY KEY  ('check_type'))"
            )
        }
    }

}