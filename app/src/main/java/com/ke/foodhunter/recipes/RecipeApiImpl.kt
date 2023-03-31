package com.ke.foodhunter.recipes

class RecipeApiImpl(private val recipeApi: RecipeApi) {
    suspend fun searchRecipes(
        appId: String,
        appKey: String,
        query: String,
        health: String?,
        ingr: Int?,
        from: Int?,
        to: Int?
    ): List<Recipe> {
        val response = recipeApi.searchRecipes(appId, appKey, query, health, ingr, from, to)
        return response.hits.map { it.recipe }
    }
}
