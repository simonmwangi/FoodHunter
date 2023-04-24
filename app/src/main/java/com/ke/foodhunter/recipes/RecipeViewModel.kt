package com.ke.foodhunter.recipes

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.HashMap


class RecipeViewModel(private val recipeApi: RecipeApiImpl) : ViewModel() {
    private var _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>>
        get() = _recipes

    private val allRecipes = mutableListOf<Recipe>()    //Holds recipe items from the Firebase Database

    private val firebaseRecipes = Firebase.database.reference.child("recipes/local/approved")

    fun searchRecipes(query: String, health: String?, ingr: Int?,diet: String?,
                      cuisineType: String?,mealType: String?, dishType: String?, calories: String?,  context: Context) {
        viewModelScope.launch {
            try {
                allRecipes.clear()  //clear the recipes list for recipes fetched from Firebsase Database

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


                val fetched = firebaseRecipes.get().await().value as HashMap<String,HashMap<*,*>>
                fetched.forEach {

                    val title = it.value["title"].toString()
                    Log.i("Happy /", "$title and $query")
                    if (title.contains(query, true)){
                        val labels= it.value["health-labels"] as List<String>
                        val images = it.value["images"] as HashMap<String, String>
                        val ingredients = it.value["ingredients"] as List<String>
                        val instructions = it.value["steps"] as List<String>
                        val saves = it.value["saves"] as Long

                        val recipe = images["img1"]?.let { it1 -> Recipe(category="Local",recipeId = it.key, saves= saves.toString(),label = title, healthLabels = labels, dietLabels = listOf("A","B"), image = it1, uri = "", ingredientLines = ingredients, instructions = instructions) }

                        if (recipe != null) allRecipes.add(recipe)
                    }


                }

                _recipes.value = allRecipes.toList() + recipes

                Log.i("Firebase-Recipes", allRecipes.toString())
                Log.i("Firebase-Recipes", _recipes.toString())

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

