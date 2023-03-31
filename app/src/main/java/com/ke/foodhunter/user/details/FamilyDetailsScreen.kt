package com.ke.foodhunter.user.details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ke.foodhunter.R
import com.ke.foodhunter.component1.rubik

@Composable
fun FamilyDataFields() {
    val defaultValue: Int = 1
    val maxValue: Int = 10

    var familyNo by remember { mutableStateOf(defaultValue.toString()) }
    var ailments by remember { mutableStateOf("") }
    var restrictions by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .padding(5.dp)
            .background(Color.DarkGray, RoundedCornerShape(16.dp))
    ) {
        OutlinedTextField(

            colors = TextFieldDefaults.outlinedTextFieldColors(
                //backgroundColor = Color.White, // does not work
                textColor = Color.Black
            ),
            value = familyNo, // #1
            textStyle = TextStyle.Default.copy(fontSize=20.sp),
            onValueChange = {it ->
                if (it.isDigitsOnly()) {
                    val value = it.toIntOrNull() ?: 0
                    if (value <= maxValue) {
                        familyNo = value.toString()
                    }
                }
            }, // #2
            placeholder = { Text("Number of Members") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(8.dp)
                .border(5.dp, Color.White, RoundedCornerShape(15.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = ailments,
            onValueChange = {text ->
                ailments = text
            },
            textStyle = TextStyle.Default.copy(fontSize=20.sp),
            placeholder = { Text("Ailments") },
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(8.dp)
                .border(5.dp, Color.White, RoundedCornerShape(15.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = restrictions,
            onValueChange = {text -> restrictions = text},
            textStyle = TextStyle.Default.copy(fontSize=20.sp),
            placeholder = { Text("Any Dietary Restrictions?") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(8.dp)
                .border(5.dp, Color.White, RoundedCornerShape(15.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun FamilyScreen(
    navController: NavController,
    route1: String,
    route2: String,
    progress: MutableState<Float>,
    viewModel: CombinedDataViewModel
){
    Column {
        Text(
            text = "Fill in your family's details here.",
            style = MaterialTheme.typography.h5,
            //fontFamily =
            fontFamily = rubik,
            color = Color(R.color.color_4),
            textAlign = TextAlign.Left,
            fontWeight = FontWeight(700.0.toInt()),
            modifier = Modifier.padding(
                start = 10.0.dp,
                top = 30.0.dp,
                end = 33.0.dp,
                bottom = 5.0.dp
            )
        )
        FamilyDataFields()
        NavigationButtons(nav = navController, route1 = route1, route2 = route2).NextButtons(progress,true)
    }
}
@Composable
@Preview(showBackground = true)
fun FamilyScreenPreview(){
    FamilyScreen(
        navController = rememberNavController(),
        "",
        "",
        progress = remember { mutableStateOf(0.0f) },
        viewModel = viewModel()
    )
}
