package com.thisthatdc.calorytracker.data.food

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.thisthatdc.calorytracker.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


sealed interface EditFoodItemEvent {
    data class SetName(val name: String) : EditFoodItemEvent
    data class SetUnit(val unit: Unit) : EditFoodItemEvent
    data class SetCalories(val calories: Int) : EditFoodItemEvent
    data class SetCarbs(val carbs: Float) : EditFoodItemEvent
    data class SetFat(val fat: Float) : EditFoodItemEvent
    data class SetProtein(val protein: Float) : EditFoodItemEvent
    data class SetSugar(val sugar: Float) : EditFoodItemEvent
    data class SetFiber(val fiber: Float) : EditFoodItemEvent

    object Save : EditFoodItemEvent
}

data class EditFoodItemState(
    val food: Food? = null,
    val name: String = "",
    val unit: Unit = Unit.GRAMS,
    val calories: Int = 0,
    val carbs: Float = 0f,
    val fat: Float = 0f,
    val protein: Float = 0f,
    val sugar: Float = 0f,
    val fiber: Float = 0f,
)

class EditFoodItemViewModel(
    private val foodDao: FoodDao,
    foodId: Long
) : ViewModel() {

    init {
        Log.d("EditFoodItemViewModel", "init $foodId")
    }


    private val _food = foodDao.getById(foodId)
    private val _state = MutableStateFlow(EditFoodItemState())

    val state = combine(_state, _food) { state, food ->
        if (state.food == null && food != null) {
            Log.d("EditFoodItemViewModel", "Got $food \n State: $state")
            _state.update {
                state.copy(
                    food = food,
                    name = food.name,
                    unit = food.unit,
                    calories = food.calories,
                    carbs = food.carbs,
                    fat = food.fat,
                    protein = food.protein,
                    sugar = food.sugar,
                )
            }
            return@combine state.copy(
                food = food,
                name = food.name,
                unit = food.unit,
                calories = food.calories,
                carbs = food.carbs,
                fat = food.fat,
                protein = food.protein,
                sugar = food.sugar,
            )
        }
        state
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        EditFoodItemState()
    )

    fun onEvent(event: EditFoodItemEvent) {
        when (event) {
            is EditFoodItemEvent.SetName -> {
               _state.update {
                    it.copy(name = event.name)
                }
            }

            is EditFoodItemEvent.SetUnit -> {
                _state.update {
                    it.copy(unit = event.unit)
                }
            }

            is EditFoodItemEvent.SetCalories -> {
                Log.d("Test", "Calories: ${event.calories}")
                _state.update {
                    it.copy(calories = event.calories)
                }
            }

            is EditFoodItemEvent.SetCarbs -> {
                _state.update {
                    it.copy(carbs = event.carbs)
                }
            }

            is EditFoodItemEvent.SetFat -> {
                _state.update {
                    it.copy(fat = event.fat)
                }
            }

            is EditFoodItemEvent.SetProtein -> {
                _state.update {
                    it.copy(protein = event.protein)
                }
            }

            is EditFoodItemEvent.SetSugar -> {
                _state.update {
                    it.copy(sugar = event.sugar)
                }
            }

            is EditFoodItemEvent.SetFiber -> {
                _state.update {
                    it.copy(fiber = event.fiber)
                }
            }

            EditFoodItemEvent.Save -> {
                Log.d("Test", "Saving: ${_state.value}")
                viewModelScope.launch {
                    val f = Food(
                        uid = _state.value.food?.uid ?: 0,
                        name = _state.value.name,
                        unit = _state.value.unit,
                        calories = _state.value.calories,
                        carbs = _state.value.carbs,
                        fat = _state.value.fat,
                        protein = _state.value.protein,
                        sugar = _state.value.sugar,
                        fiber = _state.value.fiber,
                        definedBy = DefinedBy.USER,
                    )
                    withContext(Dispatchers.IO) {
                        foodDao.upsert(f)
                    }
                }
            }
        }
    }

    companion object {
        fun Factory(foodId: Long): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val db = AppDatabase.getDatabase(application)
                return EditFoodItemViewModel(db.foodDao, foodId) as T
            }
        }
    }
}