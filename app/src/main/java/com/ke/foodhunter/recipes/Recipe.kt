package com.ke.foodhunter.recipes

data class Recipe(
    val uri: String,
    val label: String,
    val image: String,
    val healthLabels: List<String>,
    val dietLabels: List<String>
)
