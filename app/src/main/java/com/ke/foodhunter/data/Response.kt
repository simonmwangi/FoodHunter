package com.ke.foodhunter.data


data class Response (
    var ingredients: List<Ingredient>? = null,
    var remainingList: List<String>? = null,
    var exception: Exception? = null
        )