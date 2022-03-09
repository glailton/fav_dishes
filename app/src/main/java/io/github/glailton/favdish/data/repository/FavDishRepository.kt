package io.github.glailton.favdish.data.repository

import androidx.annotation.WorkerThread
import io.github.glailton.favdish.data.database.FavDishDao
import io.github.glailton.favdish.data.entities.FavDish
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavDishRepository @Inject constructor(private val favDishDao: FavDishDao) {

    @WorkerThread
    suspend fun insertFavDishData(favDish: FavDish){
        favDishDao.insertFavDishDetails(favDish)
    }

    val allDishesList: Flow<List<FavDish>> = favDishDao.getAllDishes()
}