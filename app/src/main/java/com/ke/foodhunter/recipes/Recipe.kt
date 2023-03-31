package com.ke.foodhunter.recipes

data class Recipe(
    val label: String,
    val image: String,
    val url: String,
    val yield: Int,
    val ingredients: List<String>,
    val healthLabels: List<String>
)
