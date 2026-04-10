package com.thisthatdc.calorytracker.data.settings

import androidx.room.ColumnInfo;
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity;
import androidx.room.Insert
import androidx.room.PrimaryKey;
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "settings")
data class Settings (
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "daily_calories") val calories: Int?,
)

@Dao
interface UserDao {
    @Query("SELECT * FROM settings")
    fun getAll(): Flow<List<Settings>>

    @Insert
    fun insert(settings: Settings)

    @Delete
    fun delete(user: Settings)
}
