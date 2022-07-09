package io.github.glailton.favdish.data.network

import io.github.glailton.favdish.data.entities.RandomDish
import io.github.glailton.favdish.ui.utils.Constants.API_ENDPOINT
import io.github.glailton.favdish.ui.utils.Constants.API_KEY
import io.github.glailton.favdish.ui.utils.Constants.LIMIT_LICENSE
import io.github.glailton.favdish.ui.utils.Constants.NUMBER
import io.github.glailton.favdish.ui.utils.Constants.TAGS
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomDishApi {

    @GET(API_ENDPOINT)
    fun getRandomDish(
        @Query(API_KEY) apiKey: String,
        @Query(LIMIT_LICENSE) limitLicense: Boolean,
        @Query(TAGS) tags: String,
        @Query(NUMBER) number: Int
    ): Single<RandomDish.Recipes>
}