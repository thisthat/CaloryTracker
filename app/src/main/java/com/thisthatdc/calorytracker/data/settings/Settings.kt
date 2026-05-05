package com.thisthatdc.calorytracker.data.settings

import androidx.room.ColumnInfo;
import androidx.room.Dao
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "settings")
data class Settings (
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "daily_calories") val calories: Int,
    @ColumnInfo(name = "daily_fat") val fat: Int,
    @ColumnInfo(name = "daily_protein") val protein: Int,
    @ColumnInfo(name = "daily_carbs") val carbs: Int,
    @ColumnInfo(name = "username", defaultValue="") val username: String,
    @ColumnInfo(name = "password", defaultValue="") val password: String,
)

@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings LIMIT 1")
    fun get(): Flow<Settings?>

    @Upsert
    fun upsert(settings: Settings)

}
