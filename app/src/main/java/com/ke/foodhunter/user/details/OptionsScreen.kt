package com.ke.foodhunter.user.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.relay.compose.ColumnScopeInstanceImpl.weight
import com.ke.foodhunter.component1.*
import com.ke.foodhunter.ui.theme.FoodHunterTheme
import com.ke.foodhunter.R
import com.ke.foodhunter.hellocard.montserrat
val btn_color = Color(R.color.button_color)
val bg_color = Color(R.color.background_theme)

@Composable
fun OptionButton(
    title: String,
    nav: NavController,
    route: String,
    progress: MutableState<Float>,
    viewModel: CombinedDataViewModel
){
    var getChoice by remember { mutableStateOf("") }

    Button(
        border = BorderStroke(2.dp, Color.Black),
        onClick = {
            getChoice = title
            viewModel.addToUserData(addData = CombinedData(userChoice = getChoice) )
            nav.navigate(route = route)
            progress.value += 0.3f
            //startForResult.launch(googleSignInClient?.signInIntent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .weight(1f)
            .padding(start = 16.dp, end = 16.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = btn_color
        )
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(6.dp),
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Default,
            fontSize = 15.sp
        )
    }
}
@Composable
fun MyJetpackButtons(
    nav: NavController,
    progress: MutableState<Float>,
    viewModel: CombinedDataViewModel
) {


    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OptionButton(title ="Personal", nav, route=Screens.User.route, progress, viewModel)
        Spacer(modifier = Modifier.height(16.dp))
        OptionButton(
            title ="Family",
            nav = nav,
            route = Screens.Family.route,
            progress = progress,viewModel
        )
        Spacer(modifier = Modifier.height(16.dp))
        OptionButton(
            title ="Someone Else",
            nav = nav,
            route = Screens.Options.route,
            progress = progress,
            viewModel = viewModel
        )
    }
}
@Composable
fun MainHeader(){
    Column(
        modifier = Modifier.padding(start=16.dp,end=16.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ){
        Text(
            text = "Who is the app for?",
            style = MaterialTheme.typography.h5,
            //fontFamily =
            fontFamily = rubik,
            color = Color.White,
            textAlign = TextAlign.Left,
            fontWeight = FontWeight(700.0.toInt()),
            modifier = Modifier.padding(
                start = 0.0.dp,
                top = 30.0.dp,
                end = 33.0.dp,
                bottom = 5.0.dp
            )
        )
        Text(
            text = "This will change how meals plans are created. You can change later in  the settings.",
            style = MaterialTheme.typography.body1,
            fontFamily = montserrat,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun OptionsScreen(
    navController: NavController,
    progress: MutableState<Float>,
    viewModel: CombinedDataViewModel
) {
    Box(
        modifier = Modifier
            .background(bg_color)
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly
        ){
            MainHeader()
            MyJetpackButtons(nav = navController,progress = progress,viewModel)
            Spacer(modifier = Modifier.height(20.dp))
            //NextButton(nav = navController,route1=route1,route2=route2).NextButtons(progress,false)
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OptionsScreenPreview() {
    FoodHunterTheme {

        OptionsScreen(
            navController = rememberNavController(),
            progress = remember { mutableStateOf(0.0f) },
            viewModel = viewModel()
        )
    }
}
