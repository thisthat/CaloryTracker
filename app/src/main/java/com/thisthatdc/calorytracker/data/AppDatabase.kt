package com.thisthatdc.calorytracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.thisthatdc.calorytracker.data.settings.Settings
import com.thisthatdc.calorytracker.data.settings.SettingsDao

@Database(entities = [Settings::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val settingsDao: SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "calory_tracker_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
