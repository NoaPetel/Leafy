package com.example.arrosageplante

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.arrosageplante.MainDestinations.LOGIN_ROUTE
import com.example.arrosageplante.MainDestinations.MENU_ROUTE
import com.example.arrosageplante.MainDestinations.SIGNIN_ROUTE
import com.example.arrosageplante.login.LoginScreen
import com.example.arrosageplante.menu.MenuScreen
import com.example.arrosageplante.signin.SignInScreen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String = MainDestinations.LOGIN_ROUTE,
){
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ){
        composable(
            MainDestinations.LOGIN_ROUTE,
        ){
            LoginScreen(
                onNavigateToSignIn = { navController.navigate(route = SIGNIN_ROUTE) },
                onNavigateToMenu = {navController.navigate(route = MENU_ROUTE)}
            )

        }
        composable(
            MainDestinations.SIGNIN_ROUTE,
        ){
            SignInScreen(
                onNavigateToLogIn = { navController.navigate(route = LOGIN_ROUTE)},
            )
        }
        composable(
            MainDestinations.MENU_ROUTE
        ){
            MenuScreen(

            )
        }
    }
}