package io.github.glailton.favdish.ui.dish.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.glailton.favdish.data.entities.FavDish
import io.github.glailton.favdish.data.repository.FavDishRepository
import javax.inject.Inject

@HiltViewModel
class DishesListViewModel @Inject constructor(private val favDishRepository: FavDishRepository)
    : ViewModel() {

        val allDishesList: LiveData<List<FavDish>> = favDishRepository.allDishesList.asLiveData()
}