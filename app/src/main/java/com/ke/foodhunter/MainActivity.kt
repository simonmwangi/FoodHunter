package com.ke.foodhunter

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ke.foodhunter.component1.rubik
import com.ke.foodhunter.hellocard.montserrat
import com.ke.foodhunter.ui.theme.FoodHunterTheme

data class Utility(
    val title: String,
    @DrawableRes val iconId: Int,
    val backgroundColor: Color
)


class MainActivity : ComponentActivity() {
    private var imageUri: Uri? = null
    private var extractedText: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodHunterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

//Will display the HomeScreen UI
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen() {
    // this is the most outer box that will
    // contain all the views,buttons,chips,etc.
    Box(
        modifier = Modifier
            .background(Color.DarkGray)
            .fillMaxSize()
    ) {
        Column {
            // this is how we call
            // function adding whole UI
            GreetingSection()

            //
            SuggestionSection()

            UtilitiesSection(
                utilities = listOf(
                    Utility(
                        title = "Create Meal Plan",
                        R.drawable.plate_restaurant_svg,
                        Color(R.color.color_1)
                    ),
                    Utility(
                        title = "Discover Recipes",
                        R.drawable.recipes_svg,
                        Color(R.color.color_2)
                    ),
                    Utility(
                        title = "Scan Ingredients",
                        R.drawable.scan_svgrepo_com,
                        Color(R.color.color_3)
                    ),
                    Utility(
                        title = "My Market",
                        R.drawable.shopping_bag_svg,
                        Color(R.color.color_4)
                    ),
                    Utility(
                        title = "Ask Anything",
                        R.drawable.question_circle_svg,
                        Color(R.color.color_5)
                    ),
                    Utility(
                        title = "My Account",
                        R.drawable.user_square_svg,
                        Color(R.color.color_6)
                    )
                )
            )
        }
    }
}

@Composable
fun UtilityItem(
    utility: Utility
) {
    BoxWithConstraints(
        // Box with some attributes
        modifier = Modifier
            .padding(7.5.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(5.dp))
            .background(Color.White)
    ) {

        // so , we have done with texture and
        // now just creating box and other things
        // box containing course elements
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .background(utility.backgroundColor)
        ) {
            Icon(
                painter = painterResource(id = utility.iconId),
                contentDescription = utility.title,
                tint = Color.White,
                modifier = Modifier.align(Alignment.TopStart)
                    .padding(10.dp)
                    .size(80.dp, 80.dp)
            )
            Text(
                text = utility.title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h5,
                lineHeight = 26.sp,
                modifier = Modifier.align(Alignment.BottomStart)
                    .padding(10.dp)
            )
            /*
            Text(
                text = "Start",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable {
                        // Handle the clicks
                    }
                    .align(Alignment.BottomEnd)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Green)
                    .padding(vertical = 6.dp, horizontal = 15.dp)
            )

             */
        }
    }
}

@ExperimentalFoundationApi
@Composable
// here we have just passed the list of courses
fun UtilitiesSection(utilities: List<Utility>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "My Dashboard",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(15.dp)
        )
        // we have used lazyVertically grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // it basically tells no. of cells in a row
            contentPadding = PaddingValues(start = 7.5.dp, end = 7.5.dp,bottom = 100.dp),
            modifier = Modifier.fillMaxHeight()
        ) {
            items(utilities.size) {
                // here we have to define how one of these item is look like
                // we will tell after defining item design
                // let me comment it for now and after
                // creating you just have to remove

                UtilityItem(utility = utilities[it])
            }
        }
    }
}


//Screen Header
@Composable
fun GreetingSection(
    name: String = "User"
) {
    // here we just arrange the views
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            // heading text view
            Text(
                text = "Food-Hunter",
                style = MaterialTheme.typography.h4,
                fontFamily = montserrat,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Good morning, $name",
                style = MaterialTheme.typography.h5
            )
            Text(
                text = "We wish you have a good day!",
                style = MaterialTheme.typography.body1
            )
        }
        // search icon
        Icon(
            painter = painterResource(id = R.drawable.baseline_settings_24),
            contentDescription = "Search",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun SuggestionSection(
    color: Color = Color.Blue
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(15.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color)
            .padding(horizontal = 15.dp, vertical = 20.dp)
            .fillMaxWidth()
    ) {
        Column {
            // here are two text views or we can say only text
            Text(
                text = "Weekly Plan",
                style = MaterialTheme.typography.h3
            )
            Text(
                text = "Come back later",
                fontFamily = rubik,
                style = MaterialTheme.typography.body1,
                color = Color.White
            )
        }
        Box(
            // box containing icon
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Green)
                .padding(10.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_forward),
                contentDescription = "Play",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    FoodHunterTheme {
        //ConversionWindow()
        HomeScreen()
    }
}