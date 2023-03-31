package com.ke.foodhunter.recipes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun MyListScreen(recipeViewModel: RecipeViewModel) {
    val recipes by recipeViewModel.recipes.observeAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = "",
            onValueChange = { /*TODO*/ },
            placeholder = { Text(text = "Search for recipes") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyColumn {
            items(recipes) { recipe ->
                RecipeItem(recipe = recipe)
            }
        }
    }
}

@Composable
fun RecipeItem(recipe: Recipe) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        AsyncImage(
            model = recipe.image,
            contentDescription = recipe.label,
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(4.dp))
        )

        Text(
            text = recipe.label,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = recipe.healthLabels.joinToString(),
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.padding(top = 8.dp)
        )

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "View recipe")
        }
    }
}
