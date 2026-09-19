package uz.gita.recipesapp.data.mapper

import uz.gita.recipesapp.data.source.local.room.entity.ShoppingItemEntity
import uz.gita.recipesapp.domain.module.IngredientUiData
import uz.gita.recipesapp.domain.module.RecipeDetailUiData
import uz.gita.recipesapp.domain.module.ShoppingItemUiData

fun ShoppingItemEntity.toUIData(): ShoppingItemUiData =
    ShoppingItemUiData(
        id = id,
        recipeId = recipeId,
        recipeTitle = recipeTitle,
        amount = amount,
        name = name,
        isChecked = isChecked
    )

fun RecipeDetailUiData.toShoppingEntities(
    ingredientIds: Set<Int>,
    createdAt: Long = System.currentTimeMillis()
): List<ShoppingItemEntity> =
    ingredients
        .filterIsInstance<IngredientUiData.Item>()
        .filter { it.id in ingredientIds }
        .map { ingredient ->
            ShoppingItemEntity(
                recipeId = id,
                recipeTitle = title,
                amount = ingredient.amount,
                name = ingredient.name,
                createdAt = createdAt
            )
        }
