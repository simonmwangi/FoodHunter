package com.ke.foodhunter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ke.foodhunter.ingredients.ScanActivity
import com.ke.foodhunter.ui.theme.FoodHunterTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodHunterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Logo(LocalContext.current)
                }
            }
        }
    }
}


@Composable
fun Logo(context: Context) {

    Column (horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ){
        Text(text = "Food-Hunter",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(all=5.dp)
                )
        Image(painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "logo-space",
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = Color.Red,
                    shape = CircleShape
                ),
            contentScale = ContentScale.Crop
        )
        Row {
            Text(text = "Prepare yourself...",
                fontSize = 20.sp
            )
        }
        var visible by remember {
            mutableStateOf(true)
        }
        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .size(56.dp)
                .clip(CircleShape)
                .background(color = Color.Green)
                .clickable(
                    onClick = {
                        Toast
                            .makeText(context, "OnClick", Toast.LENGTH_LONG)
                            .show()
                        Log.v("OnClick", "OnClick")
                        visible = !visible
                        GlobalScope.launch {
                            delay(2000)
                            context.startActivity(Intent(context, ScanActivity::class.java))
                            visible = true
                        }

                    }
                ),
            contentAlignment = Alignment.Center
        )
        {

            if (visible) {
                Image(painterResource(id = R.drawable.arrow_forward),
                    alignment = Alignment.Center,
                    contentDescription = "next activity")
            }else {
                visible=false
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier
                        .size(50.dp),
                    strokeWidth = 5.dp

                )
            }



        }

    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview2() {
    FoodHunterTheme {
        Logo(context = LocalContext.current)
    }
}