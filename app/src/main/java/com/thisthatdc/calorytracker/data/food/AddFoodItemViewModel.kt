package com.thisthatdc.calorytracker.data.food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.thisthatdc.calorytracker.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


sealed interface AddFoodItemEvent {
    data class SetName(val name: String) : AddFoodItemEvent
    data class SetUnit(val unit: Unit) : AddFoodItemEvent
    data class SetCalories(val calories: Int) : AddFoodItemEvent
    data class SetCarbs(val carbs: Float) : AddFoodItemEvent
    data class SetFat(val fat: Float) : AddFoodItemEvent
    data class SetProtein(val protein: Float) : AddFoodItemEvent
    data class SetSugar(val sugar: Float) : AddFoodItemEvent
    data class SetFiber(val fiber: Float) : AddFoodItemEvent

    object Save : AddFoodItemEvent
}

data class AddFoodItemState(
    val name: String = "",
    val unit: Unit = Unit.GRAMS,
    val calories: Int = 0,
    val carbs: Float = 0f,
    val fat: Float = 0f,
    val protein: Float = 0f,
    val sugar: Float = 0f,
    val fiber: Float = 0f,
)

class AddFoodItemViewModel(
    private val foodDao: FoodDao
) : ViewModel() {

    private val _state = MutableStateFlow(AddFoodItemState())

    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AddFoodItemState()
    )

    fun onEvent(event: AddFoodItemEvent) {
        when (event) {
            is AddFoodItemEvent.SetName -> {
                _state.update {
                    it.copy(name = event.name)
                }
            }

            is AddFoodItemEvent.SetUnit -> {
                _state.update {
                    it.copy(unit = event.unit)
                }
            }

            is AddFoodItemEvent.SetCalories -> {
                _state.update {
                    it.copy(calories = event.calories)
                }
            }

            is AddFoodItemEvent.SetCarbs -> {
                _state.update {
                    it.copy(carbs = event.carbs)
                }
            }

            is AddFoodItemEvent.SetFat -> {
                _state.update {
                    it.copy(fat = event.fat)
                }
            }

            is AddFoodItemEvent.SetProtein -> {
                _state.update {
                    it.copy(protein = event.protein)
                }
            }

            is AddFoodItemEvent.SetSugar -> {
                _state.update {
                    it.copy(sugar = event.sugar)
                }
            }

            is AddFoodItemEvent.SetFiber -> {
                _state.update {
                    it.copy(fiber = event.fiber)
                }
            }

            AddFoodItemEvent.Save -> {
                viewModelScope.launch {
                    val f = Food(
                        uid = 0,
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
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val db = AppDatabase.getDatabase(application)
                return AddFoodItemViewModel(db.foodDao) as T
            }
        }
    }
}