package com.ke.foodhunter.recipes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val category: String? = "Online", // Either local, online or my recipe
    var recipeId: String? = null,
    var saves: String? = null,
    val uri: String,
    val label: String,
    val image: String,
    val healthLabels: List<String>,
    val dietLabels: List<String>,
    //val images: HashMap<String,String>,
    val ingredientLines: List<String>,
    val instructions: List<String>?,
    //val ingredientLines: List<String>? = null,
    val calories: Float? = 0F,
    val glycemicIndex: Float? = 0F

) : Parcelable
