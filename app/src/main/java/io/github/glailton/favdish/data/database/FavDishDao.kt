package io.github.glailton.favdish.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.glailton.favdish.data.entities.FavDish
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDishDao {

    @Insert
    suspend fun insertFavDishDetails(favDish: FavDish)

    @Query("SELECT * FROM FAV_DISHES ORDER BY ID")
    fun getAllDishes(): Flow<List<FavDish>>
}