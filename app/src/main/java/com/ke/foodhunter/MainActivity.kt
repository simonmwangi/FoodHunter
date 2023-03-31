package com.ke.foodhunter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ke.foodhunter.component1.rubik
import com.ke.foodhunter.hellocard.montserrat
import com.ke.foodhunter.ingredients.ScanActivity
import com.ke.foodhunter.recipes.DiscoverRecipesActivity
import com.ke.foodhunter.ui.theme.FoodHunterTheme
import com.ke.foodhunter.user.details.CombinedData

data class Utility(
    val title: String,
    @DrawableRes val iconId: Int,
    val backgroundColor: Color,
    val intent: Class<*>
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mAuth = FirebaseAuth.getInstance()  //get current user instance

        val defaultName = mAuth.currentUser?.displayName.toString()      //get the full name of the user instance

        //val bundle = intent?.getBundleExtra("bundle")
        val parcelable = intent?.extras?.getParcelable<CombinedData>("save_data")   //

        if (parcelable != null){    //if the intent call contained a parcelable object then call savePersonalDetails() fun
            Log.i("Checker" ,"Not Null")
            savePersonalDetails(parcelable,mAuth)
        }

        setContent {
            FoodHunterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HomeScreen(currentUser = defaultName, onLogoutClick = { onLogoutClick(mAuth) })
                }
            }
        }
    }

    //Save user personal details from collected parcelable object
    private fun savePersonalDetails(parcelable: CombinedData, mAuth: FirebaseAuth,) {
        val database: FirebaseFirestore = FirebaseFirestore.getInstance()
        val saveHashMap = HashMap<String,Any>()

        saveHashMap["choice"] = parcelable.userChoice
        saveHashMap["username"] = parcelable.username
        saveHashMap["height"] = parcelable.height
        saveHashMap["weight"] = parcelable.weight
        saveHashMap["ailments"] = parcelable.ailments
        saveHashMap["restrictions"] = parcelable.restrictions
        saveHashMap["familyNo"] = parcelable.familyNo
        saveHashMap["mainGoal"] = parcelable.mainGoal

        database.collection("users/${mAuth.currentUser?.uid}").document("personal")
            .set(saveHashMap)
            .addOnSuccessListener {
                Toast.makeText(this,"You are all set! ",Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this," Error in adding personal data. ",Toast.LENGTH_LONG).show()
            }
    }

    private fun onLogoutClick(mAuth: FirebaseAuth) {

        val googleSignInClient : GoogleSignInClient

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)

        //Sign out
        mAuth.signOut()
        googleSignInClient.signOut().addOnSuccessListener {
            Toast.makeText(this, "Successfully Signed Out",Toast.LENGTH_SHORT ).show()
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }.addOnFailureListener {
            Toast.makeText(this, "Signing Out Failed. Try Again", Toast.LENGTH_SHORT).show()
        }
    }
}

//Will display the HomeScreen UI
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(onLogoutClick: () -> Unit,currentUser: String?) {

    Box(
        modifier = Modifier
            .background(Color.DarkGray)
            .fillMaxSize()
    ) {
        Column {
            if (currentUser != null) {
                GreetingSection(onLogoutClick, name = currentUser)
            }

            //
            SuggestionSection()

            UtilitiesSection(
                utilities = listOf(
                    Utility(
                        title = "Create Meal Plan",
                        R.drawable.plate_restaurant_svg,
                        Color(R.color.color_1),
                        MainActivity::class.java
                    ),
                    Utility(
                        title = "Discover Recipes",
                        R.drawable.recipes_svg,
                        Color(R.color.color_2),
                        DiscoverRecipesActivity::class.java
                    ),
                    Utility(
                        title = "Scan Ingredients",
                        R.drawable.scan_svgrepo_com,
                        Color(R.color.color_3),
                        ScanActivity::class.java
                    ),
                    Utility(
                        title = "My Market",
                        R.drawable.shopping_bag_svg,
                        Color(R.color.color_4),
                        MainActivity::class.java
                    ),
                    Utility(
                        title = "Ask Anything",
                        R.drawable.question_circle_svg,
                        Color(R.color.color_5),
                        MainActivity::class.java
                    ),
                    Utility(
                        title = "My Account",
                        R.drawable.user_square_svg,
                        Color(R.color.color_6),
                        MainActivity::class.java
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
    val context = LocalContext.current

    BoxWithConstraints(
        // Box with some attributes
        modifier = Modifier
            .padding(7.5.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(5.dp))
            .background(Color.White)
            .clickable {

                context.startActivity(Intent(context,utility.intent))
            }
    ) {

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
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
                    .size(80.dp, 80.dp)
            )
            Text(
                text = utility.title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h5,
                lineHeight = 26.sp,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
            )
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

                UtilityItem(utility = utilities[it])    //Every card in the dashboard
            }
        }
    }
}


//Screen Header
@Composable
fun GreetingSection(
    logout: () -> Unit,
    name: String = "User"

) {
    val thisContext = LocalContext.current
    // here we just arrange the views
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Top
        ) {
            // heading text view
            Text(
                text = "Food-Hunter",
                style = MaterialTheme.typography.h4,
                fontFamily = montserrat,
                fontWeight = FontWeight.ExtraBold
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
        // settings icon
        var showMenu by remember { mutableStateOf(false) }
        IconButton(onClick = { showMenu = true }, modifier = Modifier.padding(end=10.dp).align(alignment = Alignment.Top)) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_settings_24),
                contentDescription = "Search",
                tint = Color.White,
                modifier = Modifier.size(24.dp)


            )
            if (showMenu) {
                DropdownMenu(
                    expanded = true,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(onClick = {
                        showMenu = false
                        logout()
                    }) {
                        Text("LOGOUT")
                    }
                }
            }
        }


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
        HomeScreen(onLogoutClick = {},"User")
    }
}