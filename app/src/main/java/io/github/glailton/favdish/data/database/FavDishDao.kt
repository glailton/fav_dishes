package io.github.glailton.favdish.data.database

import androidx.room.*
import io.github.glailton.favdish.data.entities.FavDish
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDishDao {

    @Insert
    suspend fun insertFavDishDetails(favDish: FavDish)

    @Query("SELECT * FROM FAV_DISHES ORDER BY ID")
    fun getAllDishes(): Flow<List<FavDish>>

    @Query("SELECT * FROM FAV_DISHES WHERE favorite_dish = 1 ORDER BY ID")
    fun getAllFavoriteDishes(): Flow<List<FavDish>>

    @Query("SELECT * FROM FAV_DISHES WHERE type = :filterType ORDER BY ID")
    fun getFilteredDishesList(filterType: String): Flow<List<FavDish>>

    @Update
    suspend fun updateFavDishDetails(favDish: FavDish)

    @Delete
    suspend fun deleteFavFish(favDish: FavDish)
}