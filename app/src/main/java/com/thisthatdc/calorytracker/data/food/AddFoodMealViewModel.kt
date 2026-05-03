package com.thisthatdc.calorytracker.data.food

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


sealed interface AddFoodMealEvent {
    data class SetQuantity(val quantity: Long) : AddFoodMealEvent
    data class Save(val time: Long) : AddFoodMealEvent
}

data class AddFoodMealState(
    val quantity: Long = 0,
    val food: Food? = null,
)

class AddFoodMealViewModel(
    foodDao: FoodDao,
    private val foodEatenDao: FoodEatenDao,
    private val meal: Meals,
    foodId: Long
) : ViewModel() {

    private val _food = foodDao.getById(foodId)

    private val _state = MutableStateFlow(AddFoodMealState())

    val state =  combine(_state, _food) { state, food ->
        if(food != null) {
            _state.update {
                it.copy(
                    food = food
                )
            }
        }
        _state.value
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AddFoodMealState()
    )

    fun onEvent(event: AddFoodMealEvent) {
        when (event) {
            is AddFoodMealEvent.SetQuantity -> {
                _state.update {
                    it.copy(
                        quantity = event.quantity
                    )
                }
            }
            is AddFoodMealEvent.Save -> {
                if(_state.value.food == null) return
                viewModelScope.launch {
                    val f = FoodEaten(
                        foodId = _state.value.food!!.uid,
                        createdAt = event.time,
                        quantity = _state.value.quantity,
                        meal = meal,
                    )
                    withContext(Dispatchers.IO) {
                        foodEatenDao.upsert(f)
                    }
                }
            }
        }
    }

    companion object {
        fun Factory(meal: Meals, foodId: Long): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val db = AppDatabase.getDatabase(application)
                return AddFoodMealViewModel(db.foodDao, db.foodEatenDao, meal, foodId) as T
            }
        }
    }
}
