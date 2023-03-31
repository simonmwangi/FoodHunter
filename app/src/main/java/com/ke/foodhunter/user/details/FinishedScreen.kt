package com.ke.foodhunter.user.details

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ke.foodhunter.MainActivity
import com.ke.foodhunter.R
import com.ke.foodhunter.component1.rubik

@Composable
fun FinishedScreen(viewModel: CombinedDataViewModel) {
    val currentContext = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(R.drawable.award_congratulation_svg),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
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
                .background(
                    color = Color(R.color.color_4),
                    shape = CircleShape
                )
        ) {
            IconButton(
                onClick = {
                    val saveModel = viewModel.userData
                    currentContext.startActivity(Intent(currentContext,MainActivity::class.java).putExtra("save_data",saveModel))
                          },
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
    FinishedScreen(viewModel = viewModel())
}
