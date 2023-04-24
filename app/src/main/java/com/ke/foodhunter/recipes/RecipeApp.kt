package com.ke.foodhunter.recipes

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.ke.foodhunter.MainActivity
import com.ke.foodhunter.R
import com.ke.foodhunter.recipes.ui.theme.FoodHunterTheme
import kotlin.math.roundToInt

val labelMaps = mapOf("Balanced" to "Protein/Fat/Carb values in 15/35/50 ratio",
    "High-Fiber" to "More than 5g fiber per serving",
    "High-Protein" to "More than 50% of total calories from proteins",
    "Low-Carb" to "Less than 20% of total calories from carbs",
    "Low-Fat" to "Less than 15% of total calories from fat",
    "Low-Sodium" to "Less than 140mg Na per serving",
    "Health-Labels" to "All possible health labels generated",
    "Alcohol-Cocktail" to  "Describes an alcoholic cocktail",
    "Alcohol-Free" to "No alcohol used or contained",
    "Celery-Free" to "Does not contain celery or derivatives",
    "Crustacean-Free" to "Does not contain crustaceans (shrimp, lobster etc.) or derivatives",
    "Dairy-Free" to "No dairy; no lactose",
    "DASH" to "Dietary Approaches to Stop Hypertension diet",
    "Egg-Free" to "No eggs or products containing eggs",
    "Fish-Free" to "No fish or fish derivatives",
    "FODMAP-Free" to "Does not contain FODMAP foods",
    "Gluten-Free" to "No ingredients containing gluten",
    "Immuno-Supportive" to "Recipes which fit a science-based approach to eating to strengthen the immune system",
    "Keto-Friendly" to "Maximum 7 grams of net carbs per serving",
    "Kidney Friendly" to "Per serving – phosphorus less than 250 mg AND potassium less than 500 mg AND sodium less than 500 mg",
    "Kosher" to "Contains only ingredients allowed by the kosher diet. However it does not guarantee kosher preparation of the ingredients themselves",
    "Low Potassium" to "Less than 150mg per serving",
    "Low Sugar" to "No simple sugars – glucose, dextrose, galactose, fructose, sucrose, lactose, maltose",
    "Lupine-Free" to "Does not contain lupine or derivatives",
    "Mediterranean" to "Mediterranean diet",
    "Mollusk-Free" to "No mollusks",
    "Mustard-Free" to "Does not contain mustard or derivatives",
    "No oil added" to "No oil added except to what is contained in the basic ingredients",
    "Paleo" to "Excludes what are perceived to be agricultural products; grains, legumes, dairy products, potatoes, refined salt, refined sugar, and processed oils",
    "Peanut-Free" to "No peanuts or products containing peanuts",
    "Pecatarian" to "Does not contain meat or meat based products, can contain dairy and fish",
    "Pork-Free" to "Does not contain pork or derivatives",
    "Red-Meat-Free" to "Does not contain beef, lamb, pork, duck, goose, game, horse, and other types of red meat or products containing red meat.",
    "Sesame-Free" to "Does not contain sesame seed or derivatives",
    "Shellfish-Free" to "No shellfish or shellfish derivatives",
    "Soy-Free" to "No soy or products containing soy",
    "Sugar-Conscious" to "Less than 4g of sugar per serving",
    "Sulfite-Free" to "No Sulfites",
    "Tree-Nut-Free" to "No tree nuts or products containing tree nuts",
    "Vegan" to "No meat, poultry, fish, dairy, eggs or honey",
    "Vegetarian" to "No meat, poultry, or fish",
    "Wheat-Free" to "No wheat, can have gluten though"

)
val healthLabelMaps = mapOf(
    "alcohol-cocktail" to  "Describes an alcoholic cocktail",
    "alcohol-free" to "No alcohol used or contained",
    "celery-free" to "Does not contain celery or derivatives",
    "crustacean-free" to "Does not contain crustaceans (shrimp, lobster etc.) or derivatives",
    "dairy-free" to "No dairy; no lactose",
    "DASH" to "Dietary Approaches to Stop Hypertension diet",
    "egg-free" to "No eggs or products containing eggs",
    "fish-free" to "No fish or fish derivatives",
    "fodmap-free" to "Does not contain FODMAP foods",
    "gluten-free" to "No ingredients containing gluten",
    "immuno-supportive" to "Recipes which fit a science-based approach to eating to strengthen the immune system",
    "keto-friendly" to "Maximum 7 grams of net carbs per serving",
    "kidney-friendly" to "Per serving – phosphorus less than 250 mg AND potassium less than 500 mg AND sodium less than 500 mg",
    "kosher" to "Contains only ingredients allowed by the kosher diet. However it does not guarantee kosher preparation of the ingredients themselves",
    "low-potassium" to "Less than 150mg per serving",
    "low-sugar" to "No simple sugars – glucose, dextrose, galactose, fructose, sucrose, lactose, maltose",
    "lupine-free" to "Does not contain lupine or derivatives",
    "Mediterranean" to "Mediteranean diet",
    "mollusk-free" to "No mollusks",
    "mustard-free" to "Does not contain mustard or derivatives",
    "No-oil-added" to "No oil added except to what is contained in the basic ingredients",
    "paleo" to "Excludes what are perceived to be agricultural products; grains, legumes, dairy products, potatoes, refined salt, refined sugar, and processed oils",
    "peanut-free" to "No peanuts or products containing peanuts",
    "pecatarian" to "Does not contain meat or meat based products, can contain dairy and fish",
    "pork-free" to "Does not contain pork or derivatives",
    "red-meat-free" to "Does not contain beef, lamb, pork, duck, goose, game, horse, and other types of red meat or products containing red meat.",
    "sesame-free" to "Does not contain sesame seed or derivatives",
    "shellfish-free" to "No shellfish or shellfish derivatives",
    "soy-free" to "No soy or products containing soy",
    "sugar-conscious" to "Less than 4g of sugar per serving",
    "sulfite-free" to "No Sulfites",
    "tree-nut-free" to "No tree nuts or products containing tree nuts",
    "vegan" to "No meat, poultry, fish, dairy, eggs or honey",
    "vegetarian" to "No meat, poultry, or fish",
    "wheat-free" to "No wheat, can have gluten though"

)

fun updateRecipeSave(context: Context, recipe: Recipe,isPressed:Boolean, newLikes: Int) {
    val database = Firebase.database
    val recipeRef = database.getReference("recipes/local/approved").child(recipe.recipeId!!)

    recipeRef.child("saves").setValue(newLikes)

    val userRef = FirebaseAuth.getInstance()
    val userFirestoreRef = FirebaseFirestore.getInstance()

    println("The value of one is $newLikes")
    if(isPressed){
        userFirestoreRef.collection("users").document("${userRef.currentUser?.uid}").collection("saved recipes").document(recipe.recipeId!!)
            .set(mapOf("label" to recipe.label, "image" to recipe.image))
            .addOnSuccessListener { Toast.makeText(context,"Recipe has been saved", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { Toast.makeText(context,"Error in saving the recipe", Toast.LENGTH_SHORT).show() }

    }else{
        userFirestoreRef.collection("users").document("${userRef.currentUser?.uid}").collection("saved recipes").document(recipe.recipeId!!)
            .delete()
            .addOnSuccessListener { Toast.makeText(context,"Recipe removed", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { Toast.makeText(context,"Error in removing the recipe", Toast.LENGTH_SHORT).show() }

    }


}

@Composable
fun RecipeApp(recipeViewModel: RecipeViewModel) {
    val context = LocalContext.current

    val recipes by recipeViewModel.recipes.observeAsState(emptyList())
    var mySearch by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    var showSearchDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background)) {
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
                            health = null,
                            ingr = null,
                            diet = null,
                            cuisineType = null,
                            mealType = null,
                            dishType = null,
                            calories = null,
                            context
                        )
                        focusManager.clearFocus()
                    }
                )
            )
            Button(
                onClick = {
                    //Search for recipes based on the new query
                    recipeViewModel.searchRecipes(
                        query = mySearch,
                        health = null,
                        ingr = null,
                        diet = null,
                        cuisineType = null,
                        mealType = null,
                        dishType = null,
                        calories = null,
                        context
                    )
                    focusManager.clearFocus()

                }
            ) {
                Icon(Icons.Default.Search, tint = Color.White, contentDescription = null)
            }
        }
        Button(modifier = Modifier.padding(10.dp),
            onClick = {
                showSearchDialog = true
            }
        ) {
            Text(text="Change My Search Preferences.")
            Icon(Icons.Default.Create, tint = Color.White, contentDescription = "Search")
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(recipes) { recipe ->
                RecipeCard(recipe)
            }
        }
    }

    if(showSearchDialog){
        AdvancedRecipeSearch(recipeViewModel) {
            showSearchDialog = false
        }
    }
}

@Composable
fun RecipeAdditionalInfo(title: String,imgVector: Int ){
    Icon(
        painterResource(id = imgVector),
        contentDescription = null,
        tint = Color.Gray,
        modifier = Modifier.size(18.dp)
    )
    Spacer(modifier = Modifier.width(width = 6.dp))
    Text(
        text = title,
        color = Color.Gray,
        fontSize = 15.sp
    )
    Spacer(modifier = Modifier.width(width = 6.dp))
}


@Composable
fun RecipeCard(recipe: Recipe) {
    var showLabelDialog by remember { mutableStateOf(false) }
    var clickedLabel by remember { mutableStateOf("none") }

    var isPressed by remember { mutableStateOf(false) }
    var bookmarkIcon = if (isPressed) R.drawable.bookmark_on else R.drawable.bookmark_off
    var save_nums by remember { mutableStateOf(recipe.saves?.toInt()) }
    var saveState = if (isPressed) "SAVED" else "SAVE"
    var finalText by remember { mutableStateOf("") }

    if (save_nums.toString() == "null") finalText = saveState else finalText = "$save_nums $saveState"

    val context  = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp,
        backgroundColor = MaterialTheme.colors.primary
    ) {

        Column (modifier = Modifier
            .fillMaxWidth()
        )
        {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
            )
            {
                AsyncImage(
                    model = recipe.image,
                    placeholder = painterResource(id = R.drawable.image_placeholder),
                    error = painterResource(id = R.drawable.image_placeholder),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    contentDescription = recipe.label,
                )
                Text("recipe.category", modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 50.dp))
                Text(text = "Date",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 10.dp)
                )
            }


            AsyncImage(
                model = recipe.image,
                placeholder = painterResource(id = R.drawable.image_placeholder),
                error = painterResource(id = R.drawable.image_placeholder),
                modifier = Modifier
                    .height(170.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop,
                contentDescription = recipe.label,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ){
                OutlinedButton(
                    enabled = if (save_nums.toString() == "null") false else true,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                    modifier = Modifier.align(Alignment.CenterStart),
                    border = BorderStroke(3.dp, color = Color.Gray),
                    onClick = {
                        isPressed = !isPressed
                        save_nums = save_nums?.plus(if (isPressed) 1 else -1)

                        if(!recipe.recipeId.isNullOrEmpty()){
                            updateRecipeSave(context,recipe, isPressed,save_nums!!.toInt())

                        }
                    }
                ){
                    Icon(
                        painterResource(id = bookmarkIcon) , "", tint = Color.Gray, modifier = Modifier
                            .size(25.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(finalText, color = Color.Gray)
                }


                Button(
                    onClick = {
                        val intent = Intent(context, FullRecipeDetailsActivity::class.java )
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        intent.putExtra("saveState", saveState)
                        intent.putExtra("recipeData" ,recipe)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .align(Alignment.CenterEnd),
                ) {
                    Icon(
                        painterResource(id = R.drawable.view_svg) , "", tint = Color.White, modifier = Modifier
                            .size(25.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "VIEW")
                }
            }
                Column(
                    modifier = Modifier.fillMaxHeight() //.weight(1f)

                ) {
                    Text(
                        text = recipe.label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "diet-labels (${recipe.dietLabels.size})",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onPrimary,
                            modifier = Modifier.padding(10.dp)
                        )
                        LazyHorizontalGrid(
                            rows = GridCells.Adaptive(100.dp),
                            contentPadding = PaddingValues(start = 10.dp, end = 10.dp),
                            modifier = Modifier.height(35.dp)
                        ) {
                            items(recipe.dietLabels.size) { index ->
                                Card(
                                    backgroundColor = MaterialTheme.colors.surface,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 5.dp)
                                        .clickable {
                                            clickedLabel = recipe.dietLabels[index]
                                            showLabelDialog = true
                                        }
                                ) {
                                    Text(
                                        recipe.dietLabels[index],
                                        style = MaterialTheme.typography.subtitle1,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(5.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = "health-labels (${recipe.healthLabels.size})",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onPrimary,
                            modifier = Modifier.padding(10.dp)
                        )
                        LazyHorizontalGrid(
                            rows = GridCells.Adaptive(100.dp),
                            contentPadding = PaddingValues(start = 10.dp, end = 10.dp),
                            modifier = Modifier.height(35.dp)
                        ) {
                            items(recipe.healthLabels.size) { index ->
                                Card(
                                    backgroundColor = MaterialTheme.colors.surface,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 5.dp)
                                        .clickable {
                                            clickedLabel = recipe.healthLabels[index]
                                            showLabelDialog = true
                                        }
                                ) {
                                    Text(
                                        recipe.healthLabels[index],
                                        style = MaterialTheme.typography.subtitle1,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(5.dp)
                                    )
                                }
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                        ) {

                            val calories = if (recipe.calories?.equals(0F)!!) "Not Available" else "${recipe.calories.roundToInt()} Cal"

                            RecipeAdditionalInfo(
                                title = "Not Available",
                                imgVector = R.drawable.clock_svg
                            )
                            RecipeAdditionalInfo(
                                title = "Not Available",
                                imgVector = R.drawable.serving_svg
                            )
                            RecipeAdditionalInfo(title = calories, imgVector = R.drawable.flame_svg)
                        }
                    }
                }


        }
    }
    if (showLabelDialog){
        //MoreDetailsDialog(label = clickedLabel)
        //showLabelDialog = false
        Dialog(onDismissRequest = { showLabelDialog = false },
            properties = DialogProperties(dismissOnBackPress = true,dismissOnClickOutside = true)
        ) {
            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = 8.dp
            ){
                Column(
                    Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.surface)
                ) {
                    Button(modifier = Modifier.padding(5.dp),
                        onClick = {
                            showLabelDialog = false
                        }) {
                        Icon(Icons.Default.ArrowBack, tint = Color.White, contentDescription = null)
                    }
                    Text(text = clickedLabel, textAlign = TextAlign.Center, style = MaterialTheme.typography.h5)

                    labelMaps[clickedLabel]?.let {
                        Text(text = it, textAlign = TextAlign.Center, style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(10.dp))
                    }
                }
            }
        }
    }


}


//Adds more variables to the recipe search
@Composable
fun AdvancedRecipeSearch(recipeViewModel: RecipeViewModel, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var searchedFood by remember { mutableStateOf("")}
    var ingredients by remember { mutableStateOf("") }
    var diet by remember { mutableStateOf("") }
    var health by remember { mutableStateOf("") }
    var cuisineType by remember { mutableStateOf("") }
    var mealType by remember { mutableStateOf("") }
    var dishType by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }

    //Get search variables
    Dialog(onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnClickOutside = false)) {
        Card(
            //shape = MaterialTheme.shapes.medium,
            shape = RoundedCornerShape(10.dp),
            // modifier = modifier.size(280.dp, 240.dp)
            modifier = Modifier.padding(8.dp),
            elevation = 8.dp
        ) {
            Column(
                Modifier
                    .background(Color.White)
            ) {

                Text(
                    text = "Search your favorite food",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 20.sp
                )

                OutlinedTextField(
                    value = searchedFood,
                    onValueChange = { searchedFood = it }, modifier = Modifier.padding(8.dp),
                    label = { Text("Search for...") }
                )
                OutlinedTextField(
                    value = ingredients,
                    onValueChange = { ingredients = it},
                    modifier = Modifier.padding(8.dp),
                    label = { Text("Max Number of ingredients")},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    )
                )


                diet = dropdownMenuBox(label = "Diet",arrayItems = arrayOf("high-protein","low-carb","low-fat","low-sodium","high-fiber","balanced"))
                health = dropdownMenuBox(label = "Health",arrayItems = healthLabelMaps.keys.toTypedArray())
                cuisineType = dropdownMenuBox(label = "Cuisine type",arrayItems = arrayOf("american","asian","british","caribbean","central europe","chinese","eastern europe","french","greek","indian","italian","japanese","korean","kosher","mediterranean","mexican","middle eastern","nordic","south american","south east asian","world"))
                mealType = dropdownMenuBox(label = "Meal type",arrayItems = arrayOf("breakfast","brunch","lunch/dinner","snack","teatime"))
                dishType = dropdownMenuBox(label = "Dish type",arrayItems = arrayOf("alcohol cocktail","biscuits and cookies","bread","cereals","condiments and sauces","desserts","drinks","egg","ice cream and custard","main course","salad","seafood","soup","side dish","starter","sweets"))

                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it },
                    modifier = Modifier.padding(8.dp),
                    label = { Text("Max Amount of Calories")},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    )
                )



                Row {
                    OutlinedButton(
                        onClick = { onDismiss() },
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Cancel")
                    }


                    Button(
                        onClick = {
                            //var checkCalories: Unit = {if(calories.isEmpty()){ return null} }
                            /*if (calories.isEmpty()){
                                calories = null
                            }*/
                            recipeViewModel.searchRecipes(
                                query = searchedFood,
                                health = health,
                                ingr = ingredients.toInt(),
                                diet = diet,
                                cuisineType = cuisineType,
                                mealType = mealType,
                                dishType = dishType,
                                calories = calories,
                                context
                            )
                            onDismiss() },
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Search")
                    }
                }


            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun dropdownMenuBox(arrayItems: Array<String>, label: String): String{
    val context = LocalContext.current
    // state of the menu
    var expanded by remember {
        mutableStateOf(false)
    }

    // remember the selected item
    var selectedItem by remember {
        mutableStateOf(arrayItems[0])
    }

    // box
    ExposedDropdownMenuBox(
        modifier = Modifier.padding(8.dp),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        // text field
        TextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        // menu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // this is a column scope
            // all the items are added vertically
            arrayItems.forEach { selectedOption ->
                // menu item
                DropdownMenuItem(onClick = {
                    selectedItem = selectedOption
                    Toast.makeText(context, selectedOption, Toast.LENGTH_SHORT).show()
                    expanded = false
                }) {
                    Text(text = selectedOption)
                }
            }
        }
    }
    return selectedItem
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun RecipeAppPreview(){
    FoodHunterTheme() {
        RecipeCard(recipe = Recipe("Local",
            null,"5",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8MXx8fGVufDB8fHx8&auto=format&fit=crop&w=700&q=60",
            "Spaghetti Carbonara",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8MXx8fGVufDB8fHx8&auto=format&fit=crop&w=700&q=60",
            listOf("Dairy-Free","Egg-Free","Peanut-Free","Fish-Free"),
            listOf("High-Protein","Low-Carb"),
          //  listOf("image_1","image_2"),
            listOf("ingredient 1", "ingredient 2"),
            listOf("step 1","step 2","step 3")
        ))
    }

}
