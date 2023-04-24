package com.ke.foodhunter.recipes

import android.util.Log

class RecipeApiImpl(private val recipeApi: RecipeApi) {
    suspend fun searchRecipes(
        appId: String,
        appKey: String,
        query: String,
        health: String?,
        ingr: Int?,
        from: Int?,
        to: Int?,
        diet: String?,
        cuisineType: String?,
        mealType: String?,
        dishType: String?,
        calories: String?,
        type: String
    ): List<Recipe> {
        val response = recipeApi.searchRecipes(appId, appKey, query, health, ingr, from, to,diet,cuisineType,mealType,dishType,calories, type)
        return response.hits.map {
            Log.i("HIT", it.recipe.toString())
            it.recipe
         }
    }
}

