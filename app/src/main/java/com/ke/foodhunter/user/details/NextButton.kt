package com.ke.foodhunter.user.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

class NextButton(private val nav : NavController,
                 private val route1: String,
                 private val route2: String
) {

    @Composable
     fun  NextButtons( progress: MutableState<Float>,enabled:Boolean){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                enabled = enabled,
                border = BorderStroke(2.dp, Color.Black),
                onClick = {
                    nav.navigate(route = route1)
                    progress.value -= 0.3f
                    //startForResult.launch(googleSignInClient?.signInIntent)
                },
                modifier = Modifier
                    .height(50.dp)
                    .padding(start = 16.dp, end = 16.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Green
                )
            ) {
                Text(
                    text = "BACK",
                    modifier = Modifier.padding(6.dp),
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Default,
                    fontSize = 15.sp
                )
            }
            Button(
                border = BorderStroke(2.dp, Color.Black),
                onClick = {
                    nav.navigate(route = route2)
                    progress.value += 0.3f
                    //startForResult.launch(googleSignInClient?.signInIntent)
                },
                modifier = Modifier
                    .height(50.dp)
                    .padding(start = 16.dp, end = 16.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Green
                )
            ) {
                Text(
                    text = "NEXT",
                    modifier = Modifier.padding(6.dp),
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Default,
                    fontSize = 15.sp
                )
            }
        }
    }

}