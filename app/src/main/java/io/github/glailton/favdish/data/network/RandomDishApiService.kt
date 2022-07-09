package io.github.glailton.favdish.data.network

import io.github.glailton.favdish.data.entities.RandomDish
import io.github.glailton.favdish.ui.utils.Constants
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RandomDishApiService @Inject constructor(private val api: RandomDishApi) {
    fun getRandomDish(): Single<RandomDish.Recipes> = api.getRandomDish(
        Constants.API_KEY_VALUE,
        Constants.LIMIT_LICENSE_VALUE,
        Constants.TAGS_VALUE,
        Constants.NUMBER_VALUE
    )
}