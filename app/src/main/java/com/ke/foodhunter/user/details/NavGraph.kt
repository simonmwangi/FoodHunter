package com.ke.foodhunter.user.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

//Constants for all Screen routes
val OPTIONS_ROUTE = Screens.Options.route
val GOALS_ROUTE = Screens.Goals.route
val USER_ROUTE = Screens.User.route
val FAMILY_ROUTE = Screens.Family.route
val FINISHED_ROUTE = Screens.Finished.route

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    changeProgress: MutableState<Float>
) {
    NavHost(navController = navController, startDestination = Screens.Options.route){
        composable(route = OPTIONS_ROUTE){
            OptionsScreen(navController = navController, progress = changeProgress)
        }
        composable(route = GOALS_ROUTE){
            GoalsScreen(navController = navController,route1= OPTIONS_ROUTE,route2= FINISHED_ROUTE,changeProgress)
        }
        composable(route = USER_ROUTE){
            PersonalDataScreen(navController = navController,route1= OPTIONS_ROUTE,route2= GOALS_ROUTE,changeProgress)
        }
        composable(route = FAMILY_ROUTE){
            FamilyScreen(navController = navController,route1= OPTIONS_ROUTE,route2= GOALS_ROUTE,changeProgress)
        }
        composable(route = FINISHED_ROUTE){
            FinishedScreen()
        }
    }
}