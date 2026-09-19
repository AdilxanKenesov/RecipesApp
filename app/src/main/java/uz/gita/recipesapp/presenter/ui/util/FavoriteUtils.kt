package uz.gita.recipesapp.presenter.ui.util

import uz.gita.recipesapp.domain.module.RecipeUiData

fun RecipeUiData.withFavorite(favoriteIds: Set<Int>): RecipeUiData =
    if (isFavorite == (id in favoriteIds)) this else copy(isFavorite = id in favoriteIds)

fun List<RecipeUiData>.withFavorites(favoriteIds: Set<Int>): List<RecipeUiData> =
    map { it.withFavorite(favoriteIds) }
