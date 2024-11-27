package com.example.arrosageplante

import androidx.navigation.NavHostController
import com.example.arrosageplante.MainDestinations.LOGIN_ROUTE
import com.example.arrosageplante.MainDestinations.MENU_ROUTE
import com.example.arrosageplante.MainDestinations.SETTINGS_ROUTE
import com.example.arrosageplante.MainDestinations.SIGNIN_ROUTE
import com.example.arrosageplante.MainScreens.LOGIN_SCREEN
import com.example.arrosageplante.MainScreens.MENU_SCREEN
import com.example.arrosageplante.MainScreens.SETTINGS_SCREEN
import com.example.arrosageplante.MainScreens.SIGNIN_SCREEN

private object MainScreens {
    const val LOGIN_SCREEN = "login"
    const val SIGNIN_SCREEN = "signin"
    const val MENU_SCREEN = "screen"
    const val SETTINGS_SCREEN = "settings"
}

object MainsDestinationsArgs {

}

object MainDestinations {
    const val LOGIN_ROUTE = LOGIN_SCREEN
    const val SIGNIN_ROUTE = SIGNIN_SCREEN
    const val MENU_ROUTE = MENU_SCREEN
    const val SETTINGS_ROUTE = SETTINGS_SCREEN
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
}