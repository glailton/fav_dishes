package io.github.glailton.favdish.data.repository

import androidx.annotation.WorkerThread
import io.github.glailton.favdish.data.database.FavDishDao
import io.github.glailton.favdish.data.entities.FavDish
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavDishRepository @Inject constructor(private val favDishDao: FavDishDao) {

    val allDishesList: Flow<List<FavDish>> = favDishDao.getAllDishes()
    val allFavoriteDishesList: Flow<List<FavDish>> = favDishDao.getAllFavoriteDishes()

    @WorkerThread
    suspend fun insertFavDishData(favDish: FavDish){
        favDishDao.insertFavDishDetails(favDish)
    }

    @WorkerThread
    suspend fun updateFavDishData(favDish: FavDish){
        favDishDao.updateFavDishDetails(favDish)
    }

    @WorkerThread
    suspend fun deleteFavDishData(favDish: FavDish){
        favDishDao.deleteFavFish(favDish)
    }
}