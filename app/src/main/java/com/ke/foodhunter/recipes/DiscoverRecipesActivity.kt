package com.ke.foodhunter.recipes

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ke.foodhunter.recipes.ui.theme.FoodHunterTheme
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiscoverRecipesActivity : ComponentActivity() {
    private val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client : OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(interceptor)
    }.build()

    private val recipeViewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory(
            RecipeApiImpl(
                Retrofit.Builder()
                    .baseUrl("https://api.edamam.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
                    .create(RecipeApi::class.java)
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodHunterTheme{
                RecipeApp(recipeViewModel)
                AddRecipe()
            }
        }
    }
}

@Composable
fun AddRecipe(){
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {

        ExtendedFloatingActionButton(
            modifier = Modifier
                .padding(all = 16.dp)
                .align(alignment = Alignment.BottomEnd)
                .clickable{},
            onClick = {
                Toast.makeText(context, "Add Recipe", Toast.LENGTH_SHORT).show()
                context.startActivity(Intent(context, CreateRecipeActivity::class.java))
            },
            text = { Text(text = "Share a Recipe.") },
            icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = "Add") })
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview4() {
    FoodHunterTheme {
        AddRecipe()
    }
}