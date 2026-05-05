package com.thisthatdc.calorytracker.data.food


import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

// Everything is 100 g/ml -> we normalize at input
@Serializable
enum class Unit(val unit: String) {
    GRAMS("g"),
    LIQUID("ml")
}

@Serializable
enum class DefinedBy {
    USER,
    SYSTEM
}

@Serializable
@Entity(tableName = "food")
data class Food (
    @PrimaryKey(autoGenerate = true) val uid: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "servingUnit") val unit: Unit,
    @ColumnInfo(name = "calories") val calories: Int,
    @ColumnInfo(name = "carbs") val carbs: Float,
    @ColumnInfo(name = "fat") val fat: Float,
    @ColumnInfo(name = "protein") val protein: Float,
    @ColumnInfo(name = "sugar") val sugar: Float,
    @ColumnInfo(name = "fiber") val fiber: Float,
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
    carbs = 10f,
    fat = 5f,
    protein = 25f,
    sugar = 1f,
    fiber = 80f,
    definedBy = DefinedBy.USER
))


enum class Meals(name: String) {
    Breakfast("Breakfast"),
    Lunch("Lunch"),
    Snacks("Snacks"),
    Dinner("Dinner"),
}

@Serializable
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
    @ColumnInfo(name = "carbs") val carbs: Float,
    @ColumnInfo(name = "fat") val fat: Float,
    @ColumnInfo(name = "protein") val protein: Float,
    @ColumnInfo(name = "sugar") val sugar: Float,
    @ColumnInfo(name = "fiber") val fiber: Float,
    @ColumnInfo(name = "defined_by") val definedBy: DefinedBy,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "quantity") val quantity: Long,
    @ColumnInfo(name = "meal") val meal: Meals,
)

@Dao
interface FoodEatenDao {
    @Query("SELECT * FROM food_eaten")
    fun getAll(): Flow<List<FoodEaten>>


    @Query("SELECT fe.*, f.name, f.servingUnit, f.calories, f.carbs, f.fat, f.protein, f.sugar, f.fiber, f.defined_by FROM food_eaten fe, food f WHERE fe.food_data = f.uid AND created_at BETWEEN :from AND :to")
    fun getDate(from: Long, to: Long): Flow<List<FoodState>>

    @Upsert
    fun upsert(foodEaten: FoodEaten)
}
