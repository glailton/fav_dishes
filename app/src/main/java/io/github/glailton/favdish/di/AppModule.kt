package io.github.glailton.favdish.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.glailton.favdish.data.database.AppDatabase
import io.github.glailton.favdish.data.database.FavDishDao
import io.github.glailton.favdish.data.network.RandomDishApi
import io.github.glailton.favdish.data.network.RandomDishApiService
import io.github.glailton.favdish.data.repository.FavDishRepository
import io.github.glailton.favdish.ui.utils.Constants
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideFavDishRepository(favDishDao: FavDishDao) = FavDishRepository(favDishDao)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = AppDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun provideTVShowDao(db: AppDatabase) = db.favDishDao()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideRandomDishApi(retrofit: Retrofit): RandomDishApi = retrofit.create(RandomDishApi::class.java)

    @Provides
    fun provideRandomDishApiService(api: RandomDishApi): RandomDishApiService = RandomDishApiService(api)
}