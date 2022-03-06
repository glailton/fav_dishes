package io.github.glailton.favdish.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.glailton.favdish.data.database.AppDatabase
import io.github.glailton.favdish.data.database.FavDishDao
import io.github.glailton.favdish.data.repository.FavDishRepository
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
}