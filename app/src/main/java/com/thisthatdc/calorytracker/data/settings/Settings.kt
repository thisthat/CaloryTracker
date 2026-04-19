package com.thisthatdc.calorytracker.data.settings

import androidx.room.ColumnInfo;
import androidx.room.Dao
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "settings")
data class Settings (
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "daily_calories") val calories: Int,
)

@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings LIMIT 1")
    fun get(): Flow<Settings>

    @Upsert
    fun upsert(settings: Settings)

}
