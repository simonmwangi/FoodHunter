package com.ke.foodhunter.user.details

sealed class Screens(val route: String) {
    object Goals: Screens(route = "goals_screen")
    object Options: Screens(route = "options_screen")
    object User: Screens(route = "user_screen")
    object Family: Screens(route = "family_screen")
    object Finished: Screens(route = "finished_screen")
}
