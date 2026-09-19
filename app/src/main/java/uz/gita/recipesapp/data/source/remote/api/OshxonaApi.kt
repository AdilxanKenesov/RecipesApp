package uz.gita.recipesapp.data.source.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uz.gita.recipesapp.data.source.remote.response.CategoryDto
import uz.gita.recipesapp.data.source.remote.response.PaginatedRecipesDto
import uz.gita.recipesapp.data.source.remote.response.RecipeFullDto
import uz.gita.recipesapp.data.source.remote.response.SearchResultDto

interface OshxonaApi {

    @GET("api/v1/recipes/")
    suspend fun recipes(
        @Query("lang") lang: String = ApiConstants.DEFAULT_LANG,
        @Query("page") page: Int = 0,
        @Query("per_page") perPage: Int = ApiConstants.DEFAULT_PER_PAGE
    ): Response<PaginatedRecipesDto>

    @GET("api/v1/recipes/random")
    suspend fun randomRecipe(
        @Query("lang") lang: String = ApiConstants.DEFAULT_LANG,
        @Query("category") category: String? = null
    ): Response<RecipeFullDto>

    @GET("api/v1/recipes/{recipe_id}")
    suspend fun recipe(
        @Path("recipe_id") recipeId: Int
    ): Response<RecipeFullDto>

    @GET("api/v1/categories/")
    suspend fun categories(
        @Query("lang") lang: String = ApiConstants.DEFAULT_LANG
    ): Response<List<CategoryDto>>

    @GET("api/v1/categories/{key}/recipes")
    suspend fun categoryRecipes(
        @Path("key") key: String,
        @Query("lang") lang: String = ApiConstants.DEFAULT_LANG,
        @Query("page") page: Int = 0,
        @Query("per_page") perPage: Int = ApiConstants.DEFAULT_PER_PAGE
    ): Response<PaginatedRecipesDto>

    @GET("api/v1/search/")
    suspend fun search(
        @Query("q") query: String,
        @Query("lang") lang: String = ApiConstants.DEFAULT_LANG,
        @Query("limit") limit: Int = ApiConstants.DEFAULT_SEARCH_LIMIT
    ): Response<SearchResultDto>

    @GET("api/v1/search/ingredients")
    suspend fun searchByIngredients(
        @Query("q") ingredients: String,
        @Query("lang") lang: String = ApiConstants.DEFAULT_LANG,
        @Query("limit") limit: Int = ApiConstants.DEFAULT_SEARCH_LIMIT
    ): Response<SearchResultDto>
}
