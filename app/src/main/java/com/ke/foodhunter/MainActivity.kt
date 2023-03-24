package com.ke.foodhunter

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.ke.foodhunter.ui.theme.FoodHunterTheme
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.ke.foodhunter.hellocard.HelloCard
import com.ke.foodhunter.newscard.NewsCard
import com.ke.foodhunter.newscard.View

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
                    ConversionWindow()
                }
            }
        }
    }
}

@Composable
fun ConversionWindow() {
    val context = LocalContext.current
    //val imageLoader = rememberImagePainter(data = imageUri)
    Column() {
        HelloCard(title="Space 1")
        NewsCard(view= View.HeroItem, thumbnail = painterResource(id = R.drawable.news_card_thumbnail1), headline = "1", author = "2", date = "3", onThumbnailTapped = {
            Toast.makeText(context,"Card Clicked",Toast.LENGTH_LONG ).show()
        } )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    FoodHunterTheme {
        ConversionWindow()
    }
}