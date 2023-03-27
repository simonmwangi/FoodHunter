package com.ke.foodhunter.user.details

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ke.foodhunter.R
import com.ke.foodhunter.component1.rubik


@Composable
fun OptionMenuWithCards() {
    val (selectedOption, setSelectedOption) = remember { mutableStateOf(0) }


    Column(
        Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        val titleArray = listOf(
            "Healthy Living",
            "Health Family",
            "Healthy Planet",
            "Food Knowledge",
            "Save Money",
            "For Body Building"
        )
        titleArray.forEachIndexed { index, option ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        setSelectedOption(index)
                    }
                    .border(
                        width = 4.dp,
                        color = if (selectedOption == index) Color.Red else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .size(50.dp),
                elevation = 8.dp,
            ) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(option, style = MaterialTheme.typography.h6)
                }
            }

        }
    }
}

@Composable
fun GoalsScreen(
        navController: NavController,
        route1: String,
        route2: String,
        progress: MutableState<Float>
    ) {
        val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

        val backCallback = remember {
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (progress.value == 0.4f) {
                        progress.value = 0.1f
                    } else {
                        isEnabled = false
                        //activity?.onBackPressed()
                    }
                }
            }
        }
        DisposableEffect(backDispatcher) {
            backDispatcher?.addCallback(backCallback)
            onDispose {
                backCallback.remove()
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            //contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(top = 20.dp, start = 15.dp, end = 15.dp)
            ) {
                Text(
                    text = "What is your main goal?",
                    style = MaterialTheme.typography.h5,
                    //fontFamily =
                    fontFamily = rubik,
                    color = Color(R.color.color_4),
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight(700.0.toInt()),
                )
                OptionMenuWithCards()
                NextButton(nav = navController, route1 = route1, route2 = route2).NextButtons(
                    progress,true
                )
            }
        }

}

@Composable
@Preview(showBackground = true)
fun GoalsScreenPreview() {
    GoalsScreen(navController = rememberNavController(),"","", progress = remember { mutableStateOf(0.0f) })
}