package uz.gita.recipesapp.presenter.ui.preview

import uz.gita.recipesapp.domain.module.CategoryUiData
import uz.gita.recipesapp.domain.module.IngredientUiData
import uz.gita.recipesapp.domain.module.RecipeDetailUiData
import uz.gita.recipesapp.domain.module.RecipeUiData
import uz.gita.recipesapp.domain.module.ShoppingItemUiData
import uz.gita.recipesapp.domain.module.StepUiData

object SampleData {

    val categories = listOf(
        CategoryUiData("nonushta", "Nonushta", "🍳", 140),
        CategoryUiData("shorva", "Sho'rva", "🍲", 10),
        CategoryUiData("gosht", "Go'sht", "🍖", 444),
        CategoryUiData("salat", "Salat", "🥗", 86),
        CategoryUiData("shirinlik", "Shirinlik", "🍰", 212),
        CategoryUiData("non", "Non", "🥐", 64),
        CategoryUiData("ichimlik", "Ichimlik", "🥤", 38),
        CategoryUiData("boshqa", "Boshqa", "🍽", 120),
        CategoryUiData("baliq", "Baliq", "🐟", 0),
        CategoryUiData("sabzavot", "Sabzavot", "🥦", 0),
    )

    val recipes = listOf(
        RecipeUiData(1, "osh", "Toshkent oshi", "gosht", "Go'sht", "", true, true),
        RecipeUiData(2, "manti", "Qiyma manti", "gosht", "Go'sht", "", false),
        RecipeUiData(3, "mastava", "Mastava sho'rva", "shorva", "Sho'rva", "", false),
        RecipeUiData(4, "somsa", "Qatlamali somsa", "non", "Non", "", true),
        RecipeUiData(5, "achichuk", "Achchiq-chuchuk salat", "salat", "Salat", "", false),
        RecipeUiData(6, "chalop", "Yozgi chalop", "shorva", "Sho'rva", "", false, true),
        RecipeUiData(7, "napoleon", "Napoleon torti", "shirinlik", "Shirinlik", "", true),
        RecipeUiData(8, "qatlama", "Qaymoqli qatlama", "nonushta", "Nonushta", "", false),
        RecipeUiData(9, "tuxumbarak", "Tuxumbarak", "nonushta", "Nonushta", "", false),
        RecipeUiData(10, "chuchvara", "Qaynatma chuchvara", "gosht", "Go'sht", "", true),
        RecipeUiData(11, "halim", "Halim", "gosht", "Go'sht", "", false),
        RecipeUiData(12, "kompot", "Olcha kompoti", "ichimlik", "Ichimlik", "", false),
    )

    val totalRecipeCount = 2203

    val recipeDetail = RecipeDetailUiData(
        id = 1,
        title = "Toshkent oshi",
        categoryKey = "gosht",
        categoryName = "Go'sht",
        imageUrl = "",
        videoUrl = "https://www.youtube.com/watch?v=sample",
        hasVideo = true,
        description = "Sarimsoq va no'xatli, qo'y go'shtidan tayyorlanadigan an'anaviy Toshkent oshi. " +
            "Qozonda sekin dam yeyilganda guruch donalari to'kilib turadi.",
        author = "Oshxona",
        publishedDate = "12.03.2025",
        url = "https://oshxona.uz/retsept/toshkent-oshi",
        ingredients = listOf(
            IngredientUiData.Heading("Palov uchun"),
            IngredientUiData.Item(1, "1 kg", "guruch"),
            IngredientUiData.Item(2, "800 g", "qo'y go'shti"),
            IngredientUiData.Item(3, "600 g", "sabzi"),
            IngredientUiData.Item(4, "2 dona", "piyoz"),
            IngredientUiData.Item(5, "300 ml", "paxta moyi"),
            IngredientUiData.Heading("Ziravorlar"),
            IngredientUiData.Item(6, "1 osh qoshiq", "tuz"),
            IngredientUiData.Item(7, "1 bosh", "sarimsoq"),
            IngredientUiData.Item(8, "100 g", "no'xat"),
        ),
        steps = listOf(
            StepUiData(1, "Qadam 1", "Guruchni sovuq suvda yuving va 40 daqiqa iliq suvda ivitib qo'ying.", emptyList(), 40),
            StepUiData(2, "Qadam 2", "Qozonni qizdiring, moyni quying va piyozni oltin rang bo'lguncha qovuring."),
            StepUiData(3, "Qadam 3", "Go'shtni qo'shib 15 daqiqa qovuring, so'ng sabzini soling va aralashtiring.", emptyList(), 15),
            StepUiData(4, "Qadam 4", "Suv quyib, no'xat va sarimsoqni soling, past olovda 30 daqiqa qaynating.", emptyList(), 30),
            StepUiData(5, "Qadam 5", "Guruchni tekislab soling, suv qaynagach olovni pasaytirib dam yeng."),
        ),
        isFavorite = true
    )

    val favorites = recipes.filter { it.isFavorite }

    val shoppingItems = listOf(
        ShoppingItemUiData(1, 1, "Toshkent oshi", "1 kg", "guruch"),
        ShoppingItemUiData(2, 1, "Toshkent oshi", "800 g", "qo'y go'shti", true),
        ShoppingItemUiData(3, 1, "Toshkent oshi", "600 g", "sabzi"),
        ShoppingItemUiData(4, 7, "Napoleon torti", "500 g", "un"),
        ShoppingItemUiData(5, 7, "Napoleon torti", "400 ml", "sut", true),
    )

    val recentSearches = listOf("osh", "manti", "shirinlik", "tuxum")

    val quickIngredients = listOf(
        "tuxum", "piyoz", "kartoshka", "sabzi", "guruch",
        "un", "sut", "pomidor", "go'sht", "sarimsoq"
    )
}
