package uz.gita.recipesapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import uz.gita.recipesapp.presenter.screens.allrecipes.AllRecipesContract
import uz.gita.recipesapp.presenter.screens.allrecipes.AllRecipesDirection
import uz.gita.recipesapp.presenter.screens.categories.CategoriesContract
import uz.gita.recipesapp.presenter.screens.categories.CategoriesDirection
import uz.gita.recipesapp.presenter.screens.categoryrecipes.CategoryRecipesContract
import uz.gita.recipesapp.presenter.screens.categoryrecipes.CategoryRecipesDirection
import uz.gita.recipesapp.presenter.screens.cooking.CookingContract
import uz.gita.recipesapp.presenter.screens.cooking.CookingDirection
import uz.gita.recipesapp.presenter.screens.home.HomeContract
import uz.gita.recipesapp.presenter.screens.home.HomeDirection
import uz.gita.recipesapp.presenter.screens.intro.IntroContract
import uz.gita.recipesapp.presenter.screens.intro.IntroDirection
import uz.gita.recipesapp.presenter.screens.language.LanguageContract
import uz.gita.recipesapp.presenter.screens.language.LanguageDirection
import uz.gita.recipesapp.presenter.screens.recipedetail.RecipeDetailContract
import uz.gita.recipesapp.presenter.screens.recipedetail.RecipeDetailDirection
import uz.gita.recipesapp.presenter.screens.saved.SavedContract
import uz.gita.recipesapp.presenter.screens.saved.SavedDirection
import uz.gita.recipesapp.presenter.screens.search.SearchContract
import uz.gita.recipesapp.presenter.screens.search.SearchDirection
import uz.gita.recipesapp.presenter.screens.settings.SettingsContract
import uz.gita.recipesapp.presenter.screens.settings.SettingsDirection
import uz.gita.recipesapp.presenter.screens.splash.SplashContract
import uz.gita.recipesapp.presenter.screens.splash.SplashDirection

@Module
@InstallIn(ViewModelComponent::class)
interface DirectionsModule {

    @Binds
    @ViewModelScoped
    fun bindSplashScreenDirection(impl: SplashDirection): SplashContract.Direction

    @Binds
    @ViewModelScoped
    fun bindIntroScreenDirection(impl: IntroDirection): IntroContract.Direction

    @Binds
    @ViewModelScoped
    fun bindLanguageScreenDirection(impl: LanguageDirection): LanguageContract.Direction

    @Binds
    @ViewModelScoped
    fun bindHomeScreenDirection(impl: HomeDirection): HomeContract.Direction

    @Binds
    @ViewModelScoped
    fun bindCategoriesScreenDirection(impl: CategoriesDirection): CategoriesContract.Direction

    @Binds
    @ViewModelScoped
    fun bindCategoryRecipesScreenDirection(impl: CategoryRecipesDirection): CategoryRecipesContract.Direction

    @Binds
    @ViewModelScoped
    fun bindSearchScreenDirection(impl: SearchDirection): SearchContract.Direction

    @Binds
    @ViewModelScoped
    fun bindSavedScreenDirection(impl: SavedDirection): SavedContract.Direction

    @Binds
    @ViewModelScoped
    fun bindAllRecipesScreenDirection(impl: AllRecipesDirection): AllRecipesContract.Direction

    @Binds
    @ViewModelScoped
    fun bindRecipeDetailScreenDirection(impl: RecipeDetailDirection): RecipeDetailContract.Direction

    @Binds
    @ViewModelScoped
    fun bindSettingsScreenDirection(impl: SettingsDirection): SettingsContract.Direction

    @Binds
    @ViewModelScoped
    fun bindCookingScreenDirection(impl: CookingDirection): CookingContract.Direction
}
