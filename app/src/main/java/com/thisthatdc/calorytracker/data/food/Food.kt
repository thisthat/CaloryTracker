package com.thisthatdc.calorytracker.data.food


import androidx.room.ColumnInfo;
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Query
import androidx.room.Relation
import kotlinx.coroutines.flow.Flow

// Everything is 100 g/ml -> we normalize at input
enum class Unit(val unit: String) {
    GRAMS("g"),
    LIQUID("ml")
}

@Entity(tableName = "food")
data class Food (
    @PrimaryKey val uid: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "servingUnit") val unit: Unit,
    @ColumnInfo(name = "calories") val calories: Int,
    @ColumnInfo(name = "carbs") val carbs: Int,
    @ColumnInfo(name = "fat") val fat: Int,
    @ColumnInfo(name = "protein") val protein: Int,
    @ColumnInfo(name = "sugar") val sugar: Int,
    @ColumnInfo(name = "fiber") val fiber: Int,
)

@Dao
interface FoodDao {
    @Query("SELECT * FROM food LIMIT 1")
    fun get(): Flow<Food?>
}


@Entity(tableName = "food_eaten")
data class FoodEaten (
    @PrimaryKey val uid: Long,
    @Embedded val foodDetails: Food,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "quantity") val quantity: Long,
    @ColumnInfo(name = "unit") val unit: Unit,
)

@Dao
interface FoodEatenDao {
    @Query("SELECT * FROM food_eaten LIMIT 1")
    fun get(): Flow<FoodEaten?>
    fun delete(foodEaten: FoodEaten)
}
