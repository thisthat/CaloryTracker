package com.thisthatdc.calorytracker.data.food


import androidx.room.ColumnInfo;
import androidx.room.Dao
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Query
import androidx.room.Upsert
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
    @Query("SELECT * FROM food")
    fun getAll(): Flow<List<Food>>
    @Upsert
    fun upsert(food: Food)
}


@Entity(tableName = "food_eaten")
data class FoodEaten (
    @PrimaryKey val uid: Long,
    @ColumnInfo(name = "food_data") val foodId: Long,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "quantity") val quantity: Long,
    @ColumnInfo(name = "unit") val unit: Unit,
)

@Dao
interface FoodEatenDao {
    @Query("SELECT * FROM food_eaten")
    fun get(): Flow<FoodEaten?>

}
