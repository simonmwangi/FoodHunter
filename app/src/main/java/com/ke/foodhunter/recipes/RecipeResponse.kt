package com.ke.foodhunter.recipes

data class RecipeSearchResponse(
    val hits: List<Hit>
)

data class Hit(
    val recipe: Recipe
)


