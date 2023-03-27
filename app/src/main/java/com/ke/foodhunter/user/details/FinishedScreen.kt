package com.ke.foodhunter.user.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ke.foodhunter.R
import com.ke.foodhunter.component1.rubik

@Composable
fun FinishedScreen() {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(R.drawable.award_congratulation_svg),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
                .border(
                    BorderStroke(5.dp, Color.White),
                    CircleShape
                )
                .clip(CircleShape),
            colorFilter = ColorFilter.tint(Color(R.color.color_2))
        )
        Text(
            text = "That is all!",
            style = MaterialTheme.typography.h5,
            fontFamily = rubik,
            modifier = Modifier.padding(top = 16.dp)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(top = 32.dp)
                .size(64.dp)
                .background(color = Color(R.color.color_4),
                    shape = CircleShape)
        ) {
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.Green
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun FinishedScreenPreview() {
    FinishedScreen()
}
