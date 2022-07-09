package io.github.glailton.favdish.ui.dish.favorite

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
class FavoriteDishesViewModel @Inject constructor(private val favDishRepository: FavDishRepository): ViewModel() {
    val allFavoriteDishes: LiveData<List<FavDish>> = favDishRepository.allFavoriteDishesList.asLiveData()

    fun insert(dish: FavDish) = viewModelScope.launch {
        favDishRepository.insertFavDishData(dish)
    }
}