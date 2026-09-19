package uz.gita.recipesapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.recipesapp.domain.usecase.allrecipes.AllRecipesUseCase
import uz.gita.recipesapp.domain.usecase.allrecipes.AllRecipesUseCaseImpl
import uz.gita.recipesapp.domain.usecase.categories.CategoriesUseCase
import uz.gita.recipesapp.domain.usecase.categories.CategoriesUseCaseImpl
import uz.gita.recipesapp.domain.usecase.categoryrecipes.CategoryRecipesUseCase
import uz.gita.recipesapp.domain.usecase.categoryrecipes.CategoryRecipesUseCaseImpl
import uz.gita.recipesapp.domain.usecase.cooking.CookingUseCase
import uz.gita.recipesapp.domain.usecase.cooking.CookingUseCaseImpl
import uz.gita.recipesapp.domain.usecase.home.HomeUseCase
import uz.gita.recipesapp.domain.usecase.home.HomeUseCaseImpl
import uz.gita.recipesapp.domain.usecase.language.LanguageUseCase
import uz.gita.recipesapp.domain.usecase.language.LanguageUseCaseImpl
import uz.gita.recipesapp.domain.usecase.recipedetail.RecipeDetailUseCase
import uz.gita.recipesapp.domain.usecase.recipedetail.RecipeDetailUseCaseImpl
import uz.gita.recipesapp.domain.usecase.saved.SavedUseCase
import uz.gita.recipesapp.domain.usecase.saved.SavedUseCaseImpl
import uz.gita.recipesapp.domain.usecase.search.SearchUseCase
import uz.gita.recipesapp.domain.usecase.search.SearchUseCaseImpl
import uz.gita.recipesapp.domain.usecase.settings.SettingsUseCase
import uz.gita.recipesapp.domain.usecase.settings.SettingsUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    fun bindLanguageUseCase(impl: LanguageUseCaseImpl): LanguageUseCase

    @Binds
    fun bindSettingsUseCase(impl: SettingsUseCaseImpl): SettingsUseCase

    @Binds
    fun bindHomeUseCase(impl: HomeUseCaseImpl): HomeUseCase

    @Binds
    fun bindAllRecipesUseCase(impl: AllRecipesUseCaseImpl): AllRecipesUseCase

    @Binds
    fun bindCategoriesUseCase(impl: CategoriesUseCaseImpl): CategoriesUseCase

    @Binds
    fun bindCategoryRecipesUseCase(impl: CategoryRecipesUseCaseImpl): CategoryRecipesUseCase

    @Binds
    fun bindSearchUseCase(impl: SearchUseCaseImpl): SearchUseCase

    @Binds
    fun bindRecipeDetailUseCase(impl: RecipeDetailUseCaseImpl): RecipeDetailUseCase

    @Binds
    fun bindCookingUseCase(impl: CookingUseCaseImpl): CookingUseCase

    @Binds
    fun bindSavedUseCase(impl: SavedUseCaseImpl): SavedUseCase
}
