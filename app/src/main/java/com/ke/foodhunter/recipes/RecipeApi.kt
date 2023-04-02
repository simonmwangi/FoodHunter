package com.ke.foodhunter.recipes

import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApi {
    @GET("recipes/v2")
    suspend fun searchRecipes(
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("q") query: String,
        @Query("health") health: String?,
        @Query("ingr") ingr: Int?,
        @Query("from") from: Int?,
        @Query("to") to: Int?,
        @Query("type") type: String
    ): RecipeSearchResponse
}

data class RecipeSearchResponse(
    val hits: List<Hit>
)

data class Hit(
    val recipe: Recipe
)



