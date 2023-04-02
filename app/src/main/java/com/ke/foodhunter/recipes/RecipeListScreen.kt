package com.ke.foodhunter.recipes


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ke.foodhunter.R


class MyRecipe (
    val title: String = "",
    val image: String = "",
    val description: String = "",
    val steps: String = ""

)

@Composable
fun RecipeListScreen(recipes: List<MyRecipe>, onAddRecipeClick: () -> Unit) {
    // State for the dialog to add a new recipe
    val showDialog = remember { mutableStateOf(false) }

    // State for the new recipe
    val newRecipe = remember { mutableStateOf(MyRecipe("", "", "", "")) }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(recipes) { recipe ->
                RecipeCard(recipe = recipe)
            }
        }
        FloatingActionButton(
            modifier = Modifier.padding(16.dp),
            onClick = onAddRecipeClick
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Recipe")
        }
        /*
        if (showDialog.value) {

            AddRecipeDialog(
                onDismiss = { showDialog.value = false },
                onRecipeAdded = { recipe ->
                    // Add the new recipe to the list
                    recipes += recipe
                },
                recipe = newRecipe.value,
                onRecipeChange = { recipe ->
                    // Update the state with the new recipe
                    newRecipe.value = recipe
                }
            )
        }

             */
    }
}


@Composable
fun RecipeCard(recipe: MyRecipe) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp,
        backgroundColor = Color.White
    ) {
        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)){

            AsyncImage(
                model = recipe.image,
                placeholder = painterResource(id = R.drawable.image_placeholder),
                error = painterResource(id = R.drawable.image_placeholder),
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop,
                contentDescription = recipe.title,
            )
            Row(modifier = Modifier.padding(top = 20.dp)){
                Column(
                    modifier = Modifier.weight(1f)

                ) {
                    Text(
                        text = recipe.title,
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.primary
                    )
                    Text(
                        text = recipe.description,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onBackground
                    )
                }
                IconButton(
                    onClick = { },
                    modifier = Modifier.background(
                        color = Color.Magenta,
                        shape = RoundedCornerShape(10.dp)
                    )
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Icon(Icons.Default.Add, tint = Color.White,  contentDescription = null)
                        Text(text = "SAVE", color = Color.White, style = MaterialTheme.typography.subtitle1)
                    }

                }
            }
        }
    }
}



@Composable
@Preview
fun RecipeScreenPreview(){
    val recipes = listOf(
        MyRecipe(
            "Spaghetti Carbonara",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8MXx8fGVufDB8fHx8&auto=format&fit=crop&w=700&q=60",
            "Spaghetti Carbonara is a classic Italian pasta dish that's quick and easy to make.",
            "1. Cook spaghetti according to package instructions.\n2. In a separate pan, fry bacon until crispy.\n3. In a bowl, whisk together eggs, grated Parmesan cheese, and black pepper.\n4. Drain spaghetti and add to bacon pan.\n5. Remove pan from heat and pour egg mixture over spaghetti, tossing quickly to avoid scrambling the eggs.\n6. Serve immediately, garnished with additional Parmesan cheese and black pepper."
        ),
        MyRecipe(
            "Beef Stroganoff",
            "https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8M3x8fGVufDB8fHx8&auto=format&fit=crop&w=700&q=60",
            "Beef Stroganoff is a Russian dish made with beef, sour cream, and mushrooms.",
            "1. In a large pan, cook beef until browned.\n2. Add onions and mushrooms and cook until tender.\n3. Stir in flour, beef broth, and Worcestershire sauce.\n4. Simmer for 10 minutes.\n5. Stir in sour cream and heat through.\n6. Serve over egg noodles."
        ),
        MyRecipe(
            "Chicken Fajitas",
            "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8Mnx8fGVufDB8fHx8&auto=format&fit=crop&w=700&q=60",
            "Chicken Fajitas are a Tex-Mex dish made with marinated chicken, peppers, and onions.",
            "1. In a bowl, whisk together olive oil, lime juice, cumin, chili powder, and garlic.\n2. Add chicken and marinate for at least 30 minutes.\n3. In a large pan, cook chicken until browned.\n4. Add peppers and onions and cook until tender.\n5. Serve in warm tortillas with your favorite toppings."
        )
    )
    RecipeListScreen(recipes = recipes) {}
}