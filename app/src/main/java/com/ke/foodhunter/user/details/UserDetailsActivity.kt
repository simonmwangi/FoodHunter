package com.ke.foodhunter.user.details

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ke.foodhunter.R
import com.ke.foodhunter.user.details.ui.theme.FoodHunterTheme

class UserDetailsActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            /*
            FoodHunterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                }
            }
            */
            val changeProgress = rememberSaveable { mutableStateOf(0.1f) }
            FoodHunterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LinearProgressIndicator(
                        progress = changeProgress.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp),
                        backgroundColor = Color.LightGray,
                        color = Color(R.color.color_4) //progress color
                    )
                    navController = rememberNavController()
                    SetupNavGraph(navController = navController, changeProgress)
                }
            }
        }
    }
}
