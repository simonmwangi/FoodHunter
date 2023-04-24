package com.ke.foodhunter.recipes

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.pager.*
import com.ke.foodhunter.R
import com.ke.foodhunter.recipes.ui.theme.FoodHunterTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class RecipeTabItem(
    val title: String,
    val icon: Painter,
    val screen: @Composable () -> Unit,
)

class FullRecipeDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pm = packageManager
        val bundle: Bundle? = intent.extras
        var selectedRecipe: Recipe? = null
        var saveState = "SAVE"

        bundle?.let {
            bundle.apply {
                //Recipe Data from RecipeList
                val recipeData: Recipe? = getParcelable("recipeData")
                selectedRecipe = recipeData

                saveState = this.getString("saveState").toString()
            }
        }


        setContent {
            FoodHunterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    selectedRecipe?.let { RecipeWindow(it,saveState,pm) }
                }
            }
        }
    }
}


@Composable
fun RecipeContentCard(contentTitle: String, contentValue: String){
    Card(
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier
            .padding(end = 5.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Text(
                contentTitle,
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(5.dp)
            )
            Text(
                contentValue,
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
            )
        }
    }
}
@OptIn(ExperimentalPagerApi::class)
@Composable
fun RecipeWindow(recipe: Recipe, recipeState: String, pm: PackageManager?) {
    var isPressed by remember { mutableStateOf(false) }
    var bookmarkIcon = if (isPressed || recipeState == "SAVED") R.drawable.bookmark_on else R.drawable.bookmark_off
    var save_nums by remember { mutableStateOf(recipe.saves) }
    var saveState = if (isPressed || recipeState == "SAVED") "SAVED" else "SAVE"

    val context  = LocalContext.current
    val scrollState = rememberScrollState()


    val buttonAlpha by animateFloatAsState(
        targetValue = if (scrollState.value < 5) 1f else 0f
    )

    val recipetabs = listOf(
        RecipeTabItem(
            title = "Ingredients",
            screen = { IngredientsScreen(recipe.ingredientLines) },
            icon = painterResource(id = R.drawable.ingredients_svg),
        ),
        RecipeTabItem(
            title = "Steps",
            screen = { StepsScreen(recipe) },
            icon = painterResource(id = R.drawable.instructions_svg),
        ),
        RecipeTabItem(
            title = "Health Info",
            screen = { HealthInfoScreen() },
            icon = painterResource(id = R.drawable.health_svg),
        )
    )
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()


    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        )
    {

            AsyncImage(
                model = recipe.image,
                placeholder = painterResource(id = R.drawable.image_placeholder),
                error = painterResource(id = R.drawable.image_placeholder),
                modifier = Modifier
                    .height(220.dp)
                    .fillMaxWidth()
                    .graphicsLayer {
                        translationY = 0.5f * scrollState.value
                    },
                // .animateContentSize(),
                contentScale = ContentScale.Crop,
                contentDescription = recipe.label,
            )



        Column(modifier = Modifier.background(color = MaterialTheme.colors.background)) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            {
                OutlinedButton(
                    onClick = {context.startActivity(Intent(context,DiscoverRecipesActivity::class.java))},
                    modifier = Modifier.size(35.dp)
                        .align(Alignment.CenterStart).alpha(buttonAlpha),
                    shape = CircleShape,
                    border = BorderStroke(3.dp, Color.LightGray),
                    contentPadding = PaddingValues(0.dp)
                ){
                    Icon(Icons.Default.ArrowBack, "", tint = Color.Gray, modifier = Modifier)
                }

                /*AsyncImage(
                    model = recipe.image,
                    placeholder = painterResource(id = R.drawable.image_placeholder),
                    error = painterResource(id = R.drawable.image_placeholder),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(40.dp)
                        .clip(CircleShape)
                        .alpha(0f),
                    contentScale = ContentScale.Crop,
                    contentDescription = recipe.label,
                )
                Text(recipe.category?: "Online", modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 50.dp))
                Text(text = "Date",
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 50.dp, top = 10.dp)
                )*/
                OutlinedButton(
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                    modifier = Modifier.align(Alignment.CenterEnd),
                    border = BorderStroke(3.dp, color = Color.Gray),
                    onClick = {
                        isPressed = !isPressed
                        save_nums.plus( if (isPressed) 1 else -1)

                        if(!recipe.recipeId.isNullOrEmpty() && !save_nums.isNullOrEmpty()){
                            updateRecipeSave(context,recipe,isPressed, save_nums!!.toInt())


                        }
                    }
                ){
                    Icon(painterResource(id = bookmarkIcon) , "", tint = Color.Gray, modifier = Modifier
                        .size(25.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("$saveState", color = Color.Gray)
                }


            }

            Column(modifier = Modifier
                .fillMaxSize()
            ) {
                Text(
                    text = recipe.label,
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                    RecipeAdditionalInfo(
                        title = "Not Available",
                        imgVector = R.drawable.clock_svg
                    )
                    //In the future a user will be able to toggle the number of servings
                    RecipeAdditionalInfo(
                        title = "Not Available",
                        imgVector = R.drawable.serving_svg
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    RecipeContentCard(contentTitle = "Calories", contentValue = recipe.calories?.roundToInt()
                        .toString())
                    RecipeContentCard(contentTitle = "Proteins", contentValue = "20")
                    RecipeContentCard(contentTitle = "Fats", contentValue = "50")
                    RecipeContentCard(contentTitle = "Carbs", contentValue = "70")
                    //RecipeContentCard(contentTitle = "s", contentValue = "s")

                }
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                            color = MaterialTheme.colors.secondary
                        )
                    },
                ) {
                    recipetabs.forEachIndexed { index, item ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                            icon = {
                                Icon(painter = item.icon, contentDescription = "")
                            },
                            text = {
                                Text(
                                    text = item.title,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        )
                    }
                }
                HorizontalPager(
                    count = recipetabs.size,
                    state = pagerState,
                ) {
                    recipetabs[pagerState.currentPage].screen()
                }

            }
        }




    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview6() {
    FoodHunterTheme {


        RecipeWindow(
            recipe = Recipe(
                "Local",
                null,"0",
                "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8MXx8fGVufDB8fHx8&auto=format&fit=crop&w=700&q=60",
                "Spaghetti Carbonara",
                "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8MXx8fGVufDB8fHx8&auto=format&fit=crop&w=700&q=60",
                listOf("Dairy-Free","Egg-Free","Peanut-Free","Fish-Free"),
                listOf("High-Protein","Low-Carb"),
                //  listOf("image_1","image_2"),
                listOf("ingredient 1", "ingredient 2","ingredient 3", "ingredient 4","ingredient 5", "ingredient 6",),
                listOf("step 1","step 2","step 3")
            ), "SAVE", pm = null
        )
    }
}