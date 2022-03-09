package io.github.glailton.favdish.ui.dish.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.glailton.favdish.data.entities.FavDish
import io.github.glailton.favdish.data.repository.FavDishRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDishViewModel @Inject constructor(private val favDishRepository: FavDishRepository)
    : ViewModel() {

        fun insert(dish: FavDish) = viewModelScope.launch {
            favDishRepository.insertFavDishData(dish)
        }
}