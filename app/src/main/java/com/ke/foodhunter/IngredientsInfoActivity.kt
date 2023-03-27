package com.ke.foodhunter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ke.foodhunter.ui.theme.FoodHunterTheme
import androidx.compose.foundation.lazy.items
import com.ke.foodhunter.data.Ingredient

class IngredientsInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ingredientList: List<Ingredient> = intent.getSerializableExtra("list") as List<Ingredient>
        println("Here is your list: $ingredientList")
        setContent {
            FoodHunterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    IngredientContent(ingredientList)
                }
            }
        }
    }
}



@Composable
fun IngredientContent(ingredientList: List<Ingredient>) {

    val ingredients = remember {
        ingredientList
    }
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(
            items = ingredients,
            itemContent = {
                IngredientListItem(item = it)
            })
    }
}

@Composable
fun IngredientListItem(item: Ingredient) {
    Row {
        Column() {
            Text(text = item.title, style = typography.h2)
            Text(text = item.description, style = typography.h6)

        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview3() {
    FoodHunterTheme {

        val testList = mutableListOf<Ingredient>()
        testList += (Ingredient(title = "A", description = "Number 1"))
        testList += (Ingredient(title = "B", description = "Number 2"))
        IngredientContent(ingredientList = testList)
    }
}