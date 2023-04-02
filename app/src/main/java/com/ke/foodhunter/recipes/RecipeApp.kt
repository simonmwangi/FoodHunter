package com.ke.foodhunter.recipes

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ke.foodhunter.MainActivity
import com.ke.foodhunter.R

@Composable
fun RecipeApp(recipeViewModel: RecipeViewModel) {
    val context = LocalContext.current

    val recipes by recipeViewModel.recipes.observeAsState(emptyList())
    var mySearch by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
        Button(modifier = Modifier.padding(5.dp),
            onClick = {
                //Back to dashboard
                context.startActivity(Intent(context,MainActivity::class.java))
            }) {
            Icon(Icons.Default.ArrowBack, tint = Color.White, contentDescription = null)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            OutlinedTextField(
                textStyle = MaterialTheme.typography.h6,
                value = mySearch,
                onValueChange = { query ->
                    mySearch = query
                },
                label = { Text("Search") },
                placeholder = { Text("Search recipes...")},
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
                    .padding(end = 5.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        // closes the keyboard and removes the focus from the TextField
                        recipeViewModel.searchRecipes(
                            query = mySearch,
                            health = "peanut-free",
                            ingr = 5,
                            context
                        )
                        focusManager.clearFocus()
                    }
                )
            )
            Button(
                onClick = {
                    recipeViewModel.searchRecipes(
                        query = mySearch,
                        health = "peanut-free",
                        ingr = 5,
                        context
                    )
                }
            ) {
                //Text(text="SEARCH")
                Icon(Icons.Default.Search, tint = Color.White, contentDescription = null)
            }

        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(recipes) { recipe ->
                RecipeCard(recipe)
            }
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(10.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp,
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)){

            AsyncImage(
                model = recipe.image,
                placeholder = painterResource(id = R.drawable.image_placeholder),
                error = painterResource(id = R.drawable.image_placeholder),
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop,
                contentDescription = recipe.label,
            )
            Row(modifier = Modifier.padding(top = 20.dp)){
                Column(
                    modifier = Modifier.weight(1f)

                ) {
                    Text(
                        text = recipe.label,
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.onPrimary
                    )
                    Column(modifier = Modifier.fillMaxSize()){
                        Text(
                            text = "diet-labels",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onPrimary
                        )
                        LazyHorizontalGrid(rows = GridCells.Adaptive(100.dp) ){
                            items(recipe.dietLabels.size) {index ->
                                Box(
                                    modifier = Modifier
                                        .width(80.dp)
                                        .height(20.dp)
                                        .background(MaterialTheme.colors.surface)
                                        .padding(10.dp)
                                ) {
                                    Text(recipe.dietLabels[index],
                                        style=MaterialTheme.typography.subtitle1,
                                        modifier = Modifier.align(Alignment.Center))
                                }
                            }
                        }
                        Text(
                            text = "health-labels",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onPrimary
                        )
                        LazyHorizontalGrid(rows = GridCells.Adaptive(100.dp) ){
                            items(recipe.healthLabels.size) {index ->
                                Box(
                                    modifier = Modifier
                                        .width(80.dp)
                                        .height(20.dp)
                                        .background(MaterialTheme.colors.surface)
                                        .padding(10.dp)
                                ) {
                                    Text(recipe.healthLabels[index],
                                        style=MaterialTheme.typography.subtitle1,
                                        modifier = Modifier.align(Alignment.Center))
                                }
                            }
                        }
                    }


                }
                IconButton(
                    onClick = { },
                    modifier = Modifier.background(
                        color = Color.Magenta,
                        shape = RoundedCornerShape(10.dp)
                    )
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        //Icon(Icons.Default.Add, tint = Color.White,  contentDescription = null)
                        Text(text = "View", color = Color.White, style = MaterialTheme.typography.subtitle1)
                    }

                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun RecipeAppPreview(){
    RecipeCard(recipe = Recipe(
        "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8MXx8fGVufDB8fHx8&auto=format&fit=crop&w=700&q=60",
        "Spaghetti Carbonara",
        "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8MXx8fGVufDB8fHx8&auto=format&fit=crop&w=700&q=60",
        listOf("Dairy-Free","Egg-Free","Peanut-Free","Fish-Free"),
        listOf("High-Protein","Low-Carb")
    ))
}
