package uz.gita.recipesapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.recipesapp.data.repository_impl.CategoryRepositoryImpl
import uz.gita.recipesapp.data.repository_impl.FavoriteRepositoryImpl
import uz.gita.recipesapp.data.repository_impl.NetworkRepositoryImpl
import uz.gita.recipesapp.data.repository_impl.RecipeRepositoryImpl
import uz.gita.recipesapp.data.repository_impl.SearchRepositoryImpl
import uz.gita.recipesapp.data.repository_impl.SettingsRepositoryImpl
import uz.gita.recipesapp.data.repository_impl.ShoppingRepositoryImpl
import uz.gita.recipesapp.domain.repository.CategoryRepository
import uz.gita.recipesapp.domain.repository.FavoriteRepository
import uz.gita.recipesapp.domain.repository.NetworkRepository
import uz.gita.recipesapp.domain.repository.RecipeRepository
import uz.gita.recipesapp.domain.repository.SearchRepository
import uz.gita.recipesapp.domain.repository.SettingsRepository
import uz.gita.recipesapp.domain.repository.ShoppingRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindRecipeRepository(impl: RecipeRepositoryImpl): RecipeRepository

    @Binds
    @Singleton
    fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @Singleton
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    @Binds
    @Singleton
    fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository

    @Binds
    @Singleton
    fun bindShoppingRepository(impl: ShoppingRepositoryImpl): ShoppingRepository

    @Binds
    @Singleton
    fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    @Singleton
    fun bindNetworkRepository(impl: NetworkRepositoryImpl): NetworkRepository
}
