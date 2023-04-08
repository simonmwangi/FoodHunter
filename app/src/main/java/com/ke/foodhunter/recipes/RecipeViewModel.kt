package com.ke.foodhunter.recipes

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.launch


class RecipeViewModel(private val recipeApi: RecipeApiImpl) : ViewModel() {
    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>>
        get() = _recipes

    fun searchRecipes(query: String, health: String?, ingr: Int?,diet: String?,
                      cuisineType: String?,mealType: String?, dishType: String?, calories: String?,  context: Context) {
        viewModelScope.launch {
            try {
                val recipes = recipeApi.searchRecipes(
                    appId = "a05fced1",
                    appKey = "904ac86d76d3935cde232791c2491bee",
                    query = query,
                    health = health,
                    ingr = ingr,
                    from = 0,
                    to = 1,
                    diet = diet,
                    cuisineType = cuisineType,
                    mealType = mealType,
                    dishType = dishType,
                    calories = calories,
                    type = "public"
                )
                _recipes.value = recipes
            }catch(error: Exception){
                Log.e("Fetch Error","Crashed with the following error: $error" )
                Toast.makeText(context,"An error occured. Check your internet connection.", Toast.LENGTH_SHORT).show()
            }


        }
    }
}

class RecipeViewModelFactory(private val recipeApi: RecipeApiImpl) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(recipeApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

