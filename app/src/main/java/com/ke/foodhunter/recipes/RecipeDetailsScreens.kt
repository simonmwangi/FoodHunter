package com.ke.foodhunter.recipes

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ke.foodhunter.R


@Composable
fun HealthInfoScreen() {

    Column(modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                text = "Health Info",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HealthInfoScreenPreview() {
    HealthInfoScreen()
}

@Composable
fun IngredientsScreen(ingredients: List<String>) {
    Column(modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        ingredients.forEach{
            Card(
                backgroundColor = MaterialTheme.colors.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Text(
                    it,
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IngredientsScreenPreview() {
    IngredientsScreen(listOf("A","B","C","D"))
}

@Composable
fun StepsScreen(recipe: Recipe) {
    var context = LocalContext.current
    Column(modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        if(recipe.instructions != null){
            recipe.instructions.forEach{
                Card(
                    backgroundColor = MaterialTheme.colors.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Text(
                        it,
                        style = MaterialTheme.typography.h5,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }
        }else{
            Text("For Instructions Click Below")
            Button(
                modifier = Modifier
                    .padding(start = 10.dp),
                onClick = {
                    //Open Browser with URL Link
                    val uri = Uri.parse(recipe.uri)
                    val intent = Intent(Intent.ACTION_VIEW, uri)

                    context.startActivity(intent)

                }
            ) {
                Icon(
                    painterResource(id = R.drawable.view_svg) , "", tint = Color.White, modifier = Modifier
                        .size(25.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Instructions")
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun StepsScreenPreview() {
    //StepsScreen()
}