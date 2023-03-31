package com.ke.foodhunter.recipes

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.launch


class RecipeViewModel(private val recipeApi: RecipeApiImpl) : ViewModel() {
    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>>
        get() = _recipes

    fun searchRecipes(query: String, health: String?, ingr: Int?) {
        viewModelScope.launch {
            val recipes = recipeApi.searchRecipes(
                appId = "YOUR_APP_ID",
                appKey = "YOUR_APP_KEY",
                query = query,
                health = health,
                ingr = ingr,
                from = null,
                to = null
            )
            _recipes.value = recipes
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

