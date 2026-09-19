package uz.gita.recipesapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.gita.recipesapp.data.source.local.pref.AppDatabase
import uz.gita.recipesapp.data.source.local.room.dao.FavoriteDao
import uz.gita.recipesapp.data.source.local.room.dao.ShoppingItemDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.NAME).build()

    @Provides
    @Singleton
    fun provideFavoriteDao(database: AppDatabase): FavoriteDao = database.favoriteDao()

    @Provides
    @Singleton
    fun provideShoppingItemDao(database: AppDatabase): ShoppingItemDao = database.shoppingItemDao()
}
