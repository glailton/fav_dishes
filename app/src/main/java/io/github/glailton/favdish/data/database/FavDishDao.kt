package io.github.glailton.favdish.data.database

import androidx.room.Dao
import androidx.room.Insert
import io.github.glailton.favdish.data.entities.FavDish

@Dao
interface FavDishDao {

    @Insert
    suspend fun insertFavDishDetails(favDish: FavDish)
}