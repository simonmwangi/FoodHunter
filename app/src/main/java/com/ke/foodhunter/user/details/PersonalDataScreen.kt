package com.ke.foodhunter.user.details

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ke.foodhunter.R
import com.ke.foodhunter.component1.rubik

@Composable
fun PersonalDataFields(
    viewModel: CombinedDataViewModel,
    route1: String,
    nav: NavController,
    route2: String
) {
    var username by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var ailments by remember { mutableStateOf("") }
    var restrictions by remember { mutableStateOf("") }


    var fromPreviousModel = viewModel.userData?.userChoice
    if(fromPreviousModel == null){
        fromPreviousModel = "Personal"
    }
    Log.i("Val Check","$fromPreviousModel")


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
            value = username, // #1
            textStyle = TextStyle.Default.copy(fontSize=20.sp),
            onValueChange = {newText ->
                username = newText }, // #2
            placeholder = { Text("Username") },
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(8.dp)
                .border(5.dp, Color.White, RoundedCornerShape(15.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = height,
            onValueChange = {text ->
                height = text
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle.Default.copy(fontSize=20.sp),
            placeholder = { Text("Height") },
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(8.dp)
                .border(5.dp, Color.White, RoundedCornerShape(15.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = weight,
            onValueChange = {text -> weight = text},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle.Default.copy(fontSize=20.sp),
            placeholder = { Text("Weight") },
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(8.dp)
                .border(5.dp, Color.White, RoundedCornerShape(15.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = ailments,
            onValueChange = {text -> ailments = text},
            textStyle = TextStyle.Default.copy(fontSize=20.sp),
            placeholder = { Text("Ailments",color = Color.White) },
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
            placeholder = { Text("Dietary Restrictions") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(8.dp)
                .border(3.dp, Color.Gray, RoundedCornerShape(9.dp))
        )
        Button(onClick = {
            val addToModel = CombinedData(fromPreviousModel,username, height.toFloat(),weight.toFloat(),ailments,restrictions)
            viewModel.addToUserData(addToModel)
            nav.navigate(route2)
        }) {
            Text(text="NEXT", color=Color.Black)

        }
    }
}

@Composable
fun PersonalDataScreen(
    navController: NavController,
    route1: String,
    route2: String,
    progress: MutableState<Float>,
    userChoice: String?,
    viewModel: CombinedDataViewModel
){
    Toast.makeText(LocalContext.current,"You have chosen $userChoice", Toast.LENGTH_SHORT).show()
    Box(
        modifier = Modifier.fillMaxHeight(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.padding(top=20.dp,start=10.dp,end=10.dp, bottom = 20.dp)
        ) {
            Text(
                text = "Fill in your bio to get started.",
                style = MaterialTheme.typography.h5,
                //fontFamily =
                fontFamily = rubik,
                color = Color(R.color.color_4),
                textAlign = TextAlign.Left,
                fontWeight = FontWeight(700.0.toInt()),
            )
            PersonalDataFields(viewModel = viewModel,nav = navController, route1 = route1, route2 = route2)

            //NavigationButtons(nav = navController, route1 = route1, route2 = route2).NextButtons(progress,true)
        }
    }

}

@Composable
@Preview(showBackground = true)
fun PersonalDataScreenPreview() {
    PersonalDataScreen(
        navController = rememberNavController(),
        "",
        "",
        progress = remember { mutableStateOf(0.0f) },
        "",
        viewModel = viewModel()
    )
}