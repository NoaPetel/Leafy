package com.example.arrosageplante.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ModalDrawer
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.example.arrosageplante.MainNavigationActions
import kotlinx.coroutines.CoroutineScope
import java.lang.reflect.Modifier

@Composable
fun AppModalDrawer(
    drawerState: DrawerState,
    currentRoute: String,
    navigationActions: MainNavigationActions,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    contente: @Composable () -> Unit
){
    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
                closeDrawer = { coroutineScope.launch { drawerState.close() } }
            )
        }
    ) {
        content()
    }
}

@Composable
private fun AppDrawer(
    currentRoute: String,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        DrawerHeader()
        DrawerButton(
            painter = painterResource(id = R.drawable.ic_list),
            label = stringResource(id = R.string.list_title),
            isSelected = currentRoute == TodoDestinations.TASKS_ROUTE,
            action = {
                navigateToTasks()
                closeDrawer()
            }
        )
        DrawerButton(
            painter = painterResource(id = R.drawable.ic_statistics),
            label = stringResource(id = R.string.statistics_title),
            isSelected = currentRoute == TodoDestinations.STATISTICS_ROUTE,
            action = {
                navigateToStatistics()
                closeDrawer()
            }
        )
    }
}
