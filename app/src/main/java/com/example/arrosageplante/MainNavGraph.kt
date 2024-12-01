package com.example.arrosageplante

import android.util.Log
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.arrosageplante.login.LoginScreen
import com.example.arrosageplante.viewmodel.UserViewModel
import com.example.arrosageplante.menu.MenuScreen
import com.example.arrosageplante.newDevice.NewDeviceTaskScreen
import com.example.arrosageplante.settings.SettingsScreen
import com.example.arrosageplante.signin.SignInScreen
import com.example.arrosageplante.viewmodel.ThemeViewModel
import com.example.arrosageplante.utils.AppModalDrawer
import com.example.arrosageplante.viewmodel.WateringViewModel
import com.example.arrosageplante.watering.WateringScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    startDestination: String = MainDestinations.MENU_ROUTE,
    navActions: MainNavigationActions = remember(navController) {
        MainNavigationActions(navController)
    }
){
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    val userViewModel: UserViewModel = UserViewModel()
    val wateringViewModel: WateringViewModel = WateringViewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ){

        composable(
            MainDestinations.LOGIN_ROUTE,
        ){
            LoginScreen(
                onNavigateToSignIn = { navActions.navigateToSignIn() },
                onNavigateToMenu = {navActions.navigateToMenu()},
                userViewModel = userViewModel
            )

        }

        composable(
            MainDestinations.SIGNIN_ROUTE,
        ){
            SignInScreen(
                onNavigateToLogIn = { navActions.navigateToLogIn()},
            )
        }

        composable(
            MainDestinations.MENU_ROUTE
        ){
            AppModalDrawer(
                drawerState,
                currentRoute,
                navActions,
                content = {
                    MenuScreen(
                        openDrawer = {
                            Log.d("Drawer", "Open")
                            coroutineScope.launch { drawerState.open() } },
                        onNavigateToNewDevice = { navActions.navigateToNewDevice() },
                        onNavigateToWatering = { navActions.navigateToWatering() },
                    )
                })
        }

        composable(
            MainDestinations.SETTINGS_ROUTE
        ){
            AppModalDrawer(
                drawerState,
                currentRoute,
                navActions,
                content = {
                    SettingsScreen(
                        userViewModel = userViewModel,
                        onNavigateToLogIn = { navActions.navigateToLogIn() },
                        openDrawer = {
                            Log.d("Drawer", "Open")
                            coroutineScope.launch { drawerState.open() } }
                    )
                })

        }

        composable(
            MainDestinations.NEWDEVICE_ROUTE
        ) {
            NewDeviceTaskScreen (
                onNavigateToMenu = { navActions.navigateToMenu() }
            )
        }

        composable(
            MainDestinations.WATERING_ROUTE
        ) {
            WateringScreen (
                onNavigateToMenu = { navActions.navigateToMenu() },
                wateringViewModel = wateringViewModel
            )
        }
    }
}