package com.example.student.covidstats

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [CountryEntity::class, CountryDetailEntity::class, LastCheckEntity::class, ContinentCountryEntity::class],
    version = 3,
    exportSchema = false
)
public abstract class CountryEntityRoomDatabase : RoomDatabase() {

    abstract fun countryEntityDao(): CountryEntityDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: CountryEntityRoomDatabase? = null
        private var mMigrator: Migrator = Migrator()

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
                ).addCallback(CALLBACK).build()
                INSTANCE = instance
                return instance
            }
        }

        private val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { Populate(it).execute() }

            }
        }

    }

}