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
import com.example.arrosageplante.view.LoginScreen
import com.example.arrosageplante.viewmodel.UserViewModel
import com.example.arrosageplante.view.MenuScreen
import com.example.arrosageplante.view.NewDeviceScreen
import com.example.arrosageplante.view.SettingsScreen
import com.example.arrosageplante.view.SignInScreen
import com.example.arrosageplante.utils.AppModalDrawer
import com.example.arrosageplante.view.GraphScreen
import com.example.arrosageplante.viewmodel.WateringViewModel
import com.example.arrosageplante.view.WateringScreen
import com.google.firebase.auth.FirebaseAuth
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

    val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

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
                userViewModel = userViewModel,
                auth = auth
            )

        }

        composable(
            MainDestinations.SIGNIN_ROUTE,
        ){
            SignInScreen(
                onNavigateToLogIn = { navActions.navigateToLogIn()},
                auth = auth
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
                        onNavigateToGraph = { navActions.navigateToGraph()}
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
            NewDeviceScreen (
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

        composable(
            MainDestinations.GRAPH_ROUTE
        ) {
            GraphScreen (
                onNavigateToMenu = { navActions.navigateToMenu() }
            )
        }
    }
}