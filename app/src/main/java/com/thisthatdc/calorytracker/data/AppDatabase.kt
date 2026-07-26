package com.thisthatdc.calorytracker.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.thisthatdc.calorytracker.data.food.Food
import com.thisthatdc.calorytracker.data.food.FoodDao
import com.thisthatdc.calorytracker.data.food.FoodEaten
import com.thisthatdc.calorytracker.data.food.FoodEatenDao
import com.thisthatdc.calorytracker.data.settings.GarminCalories
import com.thisthatdc.calorytracker.data.settings.GarminCaloriesDao
import com.thisthatdc.calorytracker.data.settings.Settings
import com.thisthatdc.calorytracker.data.settings.SettingsDao
import com.thisthatdc.calorytracker.util.Time

@Database(
    entities = [Settings::class, Food::class, FoodEaten::class, GarminCalories::class],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract val settingsDao: SettingsDao
    abstract val foodDao: FoodDao
    abstract val foodEatenDao: FoodEatenDao
    abstract val garminCalories: GarminCaloriesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE settings ADD COLUMN \"last_changed_at\" TEXT NOT NULL DEFAULT ''")
                db.execSQL("UPDATE settings SET \"last_changed_at\"='2026-01-01'")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "calory_tracker_db"
                ).createFromAsset("database/test.db")
                    .addMigrations(MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
