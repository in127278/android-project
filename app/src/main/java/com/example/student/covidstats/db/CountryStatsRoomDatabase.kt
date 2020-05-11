package com.example.student.covidstats.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.student.covidstats.helpers.Migrator
import com.example.student.covidstats.helpers.Populate

@Database(
    entities = [CountryEntity::class, CountryDetailEntity::class, LastCheckEntity::class, ContinentCountryEntity::class],
    version = 3,
    exportSchema = false
)
public abstract class CountryStatsRoomDatabase : RoomDatabase() {

    abstract fun countryEntityDao(): CountryStatsDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: CountryStatsRoomDatabase? = null
        private var mMigrator: Migrator =
            Migrator()

        fun getDatabase(context: Context): CountryStatsRoomDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CountryStatsRoomDatabase::class.java,
                    "country_database"
                ).addCallback(CALLBACK).build()
                INSTANCE = instance
                return instance
            }
        }

        private val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { Populate(
                    it
                ).execute() }

            }
        }

    }

}