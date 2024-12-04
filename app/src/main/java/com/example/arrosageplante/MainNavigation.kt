package com.example.arrosageplante

import androidx.navigation.NavHostController
import com.example.arrosageplante.MainDestinations.GRAPH_ROUTE
import com.example.arrosageplante.MainDestinations.LOGIN_ROUTE
import com.example.arrosageplante.MainDestinations.MENU_ROUTE
import com.example.arrosageplante.MainDestinations.NEWDEVICE_ROUTE
import com.example.arrosageplante.MainDestinations.SETTINGS_ROUTE
import com.example.arrosageplante.MainDestinations.SIGNIN_ROUTE
import com.example.arrosageplante.MainDestinations.WATERING_ROUTE
import com.example.arrosageplante.MainScreens.GRAPH_SCREEN
import com.example.arrosageplante.MainScreens.LOGIN_SCREEN
import com.example.arrosageplante.MainScreens.MENU_SCREEN
import com.example.arrosageplante.MainScreens.NEWDEVICE_SCREEN
import com.example.arrosageplante.MainScreens.SETTINGS_SCREEN
import com.example.arrosageplante.MainScreens.SIGNIN_SCREEN
import com.example.arrosageplante.MainScreens.WATERING_SCREEN

private object MainScreens {
    const val LOGIN_SCREEN = "login"
    const val SIGNIN_SCREEN = "signin"
    const val MENU_SCREEN = "screen"
    const val SETTINGS_SCREEN = "settings"
    const val NEWDEVICE_SCREEN = "newDevice"
    const val WATERING_SCREEN = "watering"
    const val GRAPH_SCREEN = "graph"
}

object MainsDestinationsArgs {

}

object MainDestinations {
    const val LOGIN_ROUTE = LOGIN_SCREEN
    const val SIGNIN_ROUTE = SIGNIN_SCREEN
    const val MENU_ROUTE = MENU_SCREEN
    const val SETTINGS_ROUTE = SETTINGS_SCREEN
    const val NEWDEVICE_ROUTE = NEWDEVICE_SCREEN
    const val WATERING_ROUTE = WATERING_SCREEN
    const val GRAPH_ROUTE = GRAPH_SCREEN
}

class MainNavigationActions(private val navController: NavHostController){
    fun navigateToSignIn(){
        navController.navigate(route = SIGNIN_ROUTE)
    }

    fun navigateToMenu(){
        navController.navigate(route = MENU_ROUTE)
    }

    fun navigateToLogIn(){
        navController.navigate(route = LOGIN_ROUTE)
    }

    fun navigateToSettings(){
        navController.navigate(route = SETTINGS_ROUTE)
    }

    fun navigateToNewDevice(){
        navController.navigate(route = NEWDEVICE_ROUTE)
    }

    fun navigateToWatering(){
        navController.navigate(route = WATERING_ROUTE)
    }

    fun navigateToGraph(){
        navController.navigate(route = GRAPH_ROUTE)
    }
}