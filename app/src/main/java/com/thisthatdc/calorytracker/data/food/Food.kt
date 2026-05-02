package com.thisthatdc.calorytracker.data.food


import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

// Everything is 100 g/ml -> we normalize at input
enum class Unit(val unit: String) {
    GRAMS("g"),
    LIQUID("ml")
}

enum class DefinedBy {
    USER,
    SYSTEM
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
    @ColumnInfo(name = "defined_by") val definedBy: DefinedBy,
)

@Dao
interface FoodDao {
    @Query("SELECT * FROM food")
    fun getAll(): Flow<List<Food>>
    @Query("SELECT * FROM food WHERE defined_by = 'USER'")
    fun getAllUserDefined(): Flow<List<Food>>
    @Query("SELECT * FROM food WHERE uid = :id")
    fun getById(id: Long): Flow<Food?>
    @Upsert
    fun upsert(food: Food)
}

val FoodExample = listOf(Food(
    uid = 1,
    name = "Cavolor Rosso",
    unit = Unit.GRAMS,
    calories = 100,
    carbs = 10,
    fat = 5,
    protein = 25,
    sugar = 1,
    fiber = 80,
    definedBy = DefinedBy.USER
))


enum class Meals(name: String) {
    Breakfast("Breakfast"),
    Lunch("Lunch"),
    Snacks("Snacks"),
    Dinner("Dinner"),
}

@Entity(tableName = "food_eaten")
data class FoodEaten (
    @PrimaryKey(autoGenerate = true) val uid: Long = 0,
    @ColumnInfo(name = "food_data") val foodId: Long,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "quantity") val quantity: Long,
    @ColumnInfo(name = "meal") val meal: Meals,
)

data class FoodState(
    @PrimaryKey val uid: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "servingUnit") val unit: Unit,
    @ColumnInfo(name = "calories") val calories: Int,
    @ColumnInfo(name = "carbs") val carbs: Int,
    @ColumnInfo(name = "fat") val fat: Int,
    @ColumnInfo(name = "protein") val protein: Int,
    @ColumnInfo(name = "sugar") val sugar: Int,
    @ColumnInfo(name = "fiber") val fiber: Int,
    @ColumnInfo(name = "defined_by") val definedBy: DefinedBy,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "quantity") val quantity: Long,
    @ColumnInfo(name = "meal") val meal: Meals,
)

@Dao
interface FoodEatenDao {
    @Query("SELECT * FROM food_eaten")
    fun get(): Flow<FoodEaten?>


    @Query("SELECT fe.*, f.name, f.servingUnit, f.calories, f.carbs, f.fat, f.protein, f.sugar, f.fiber, f.defined_by FROM food_eaten fe, food f WHERE fe.food_data = f.uid AND created_at BETWEEN :from AND :to")
    fun getDate(from: Long, to: Long): Flow<List<FoodState>>

    @Upsert
    fun upsert(foodEaten: FoodEaten)
}
