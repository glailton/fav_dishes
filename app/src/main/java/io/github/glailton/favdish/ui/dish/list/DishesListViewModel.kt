package io.github.glailton.favdish.ui.dish.list

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.glailton.favdish.data.entities.FavDish
import io.github.glailton.favdish.data.repository.FavDishRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DishesListViewModel @Inject constructor(private val favDishRepository: FavDishRepository) :
    ViewModel() {

    val allDishesList: LiveData<List<FavDish>> = favDishRepository.allDishesList.asLiveData()

    fun delete(favDish: FavDish) = viewModelScope.launch {
        favDishRepository.deleteFavDishData(favDish)
    }
}