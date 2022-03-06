package io.github.glailton.favdish.ui.dish.add

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.glailton.favdish.data.repository.FavDishRepository
import javax.inject.Inject

@HiltViewModel
class AddDishViewModel @Inject constructor(private val favDishRepository: FavDishRepository)
    : ViewModel() {

}