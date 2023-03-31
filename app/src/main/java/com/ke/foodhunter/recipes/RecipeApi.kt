package com.ke.foodhunter.recipes

import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApi {
    @GET("recipes")
    suspend fun searchRecipes(
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("q") query: String,
        @Query("health") health: String?,
        @Query("ingr") ingr: Int?,
        @Query("from") from: Int?,
        @Query("to") to: Int?
    ): RecipeSearchResponse
}

