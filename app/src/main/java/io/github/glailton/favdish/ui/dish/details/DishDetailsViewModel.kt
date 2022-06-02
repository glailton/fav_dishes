package io.github.glailton.favdish.ui.dish.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.glailton.favdish.data.entities.FavDish
import io.github.glailton.favdish.data.repository.FavDishRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DishDetailsViewModel @Inject constructor(private val favDishRepository: FavDishRepository): ViewModel() {

    fun update(dish: FavDish) = viewModelScope.launch {
        favDishRepository.updateFavDishData(dish)
    }
}