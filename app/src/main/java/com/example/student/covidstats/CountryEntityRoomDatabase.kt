package com.example.student.covidstats

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(CountryEntity::class), version = 1, exportSchema = false)
public abstract class CountryEntityRoomDatabase : RoomDatabase() {

    abstract fun countryEntityDao(): CountryEntityDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: CountryEntityRoomDatabase? = null

        fun getDatabase(context: Context): CountryEntityRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CountryEntityRoomDatabase::class.java,
                    "country_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}