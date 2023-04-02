package com.ke.foodhunter.recipes

class RecipeApiImpl(private val recipeApi: RecipeApi) {
    suspend fun searchRecipes(
        appId: String,
        appKey: String,
        query: String,
        health: String?,
        ingr: Int?,
        from: Int?,
        to: Int?,
        type: String
    ): List<Recipe> {
        val response = recipeApi.searchRecipes(appId, appKey, query, health, ingr, from, to, type)
        return response.hits.map { it.recipe }
    }
}

