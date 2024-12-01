package com.example.arrosageplante.settings

import android.content.res.Resources.Theme
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arrosageplante.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.arrosageplante.viewmodel.ThemeViewModel
import com.example.arrosageplante.utils.SettingsTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    userViewModel: UserViewModel,
    onNavigateToLogIn: () -> Unit,
    openDrawer: () -> Unit,
    themeViewModel: ThemeViewModel = ThemeViewModel()
) {
    val userData by userViewModel.userData.collectAsStateWithLifecycle()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState(false)

    Scaffold(
        topBar = {
            SettingsTopAppBar (openDrawer = openDrawer)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // User Information Section
            Text("Informations du compte", style = MaterialTheme.typography.titleMedium)

            if (userData != null) {
                SettingsItem(
                    title = "Email",
                    value = userData?.email ?: "Email non disponible"
                )
            } else {
                Text(
                    text = "Aucune information utilisateur disponible",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }



            Spacer(modifier = Modifier.height(16.dp))
            Text("Préférences", style = MaterialTheme.typography.titleMedium)

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                thickness = 1.dp
            )

            SettingsSwitch(
                title = "Mode sombre",
                checked = isDarkTheme,
                onCheckedChange = {
                    Log.d("Settings","Toggled")
                    themeViewModel.toggleTheme()
                }

            )


            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                thickness = 1.dp
            )

            // Logout Button
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    // Sign out the user
                    FirebaseAuth.getInstance().signOut()
                    onNavigateToLogIn()
                }
            ) {
                Text("Déconnexion")
            }
        }
    }
}

// Reusable Settings Item Composable
@Composable
fun SettingsItem(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.bodyLarge)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

// Reusable Settings Switch Composable
@Composable
fun SettingsSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.bodyLarge)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}