package com.thisthatdc.calorytracker.data.food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.thisthatdc.calorytracker.data.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn


sealed interface AddFoodEvent {
    data class SetName(val name: String) : AddFoodEvent
}

class AddFoodViewModel(
    foodDao: FoodDao,
) : ViewModel() {

    private val _state = MutableStateFlow(AddFoodItemState())

    val foods = foodDao.getAll().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AddFoodItemState()
    )

    fun onEvent(event: AddFoodEvent) {
        when (event) {
            is AddFoodEvent.SetName -> {
                // TODO
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
                return AddFoodViewModel(db.foodDao) as T
            }
        }
    }
}