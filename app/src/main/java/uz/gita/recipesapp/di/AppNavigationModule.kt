package uz.gita.recipesapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.recipesapp.navigation.AppNavigationDispatcher
import uz.gita.recipesapp.navigation.AppNavigationHandler
import uz.gita.recipesapp.navigation.AppNavigator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppNavigationModule {

    @Provides
    @Singleton
    fun provideAppNavigator(): AppNavigator = AppNavigationDispatcher

    @Provides
    @Singleton
    fun provideAppNavigationHandler(): AppNavigationHandler = AppNavigationDispatcher
}