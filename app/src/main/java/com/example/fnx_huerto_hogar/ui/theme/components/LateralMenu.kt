package com.example.fnx_huerto_hogar.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.fnx_huerto_hogar.R
import com.example.fnx_huerto_hogar.navigate.AppScreens
import com.example.fnx_huerto_hogar.navigate.AppNavHost
import kotlinx.coroutines.launch

@Composable
fun LateralMenu() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // Logo cabecero
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cabecero),
                        contentDescription = "Logo Huerto Hogar",
                        modifier = Modifier
                            .size(160.dp)
                            .padding(bottom = 2.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                Spacer(modifier = Modifier.padding(vertical = 4.dp))

                // Sección de usuario
                NavigationDrawerItem(
                    label = { Text("Iniciar Sesión") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(AppScreens.LoginScreen.route)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = "Iniciar Sesión"
                        )
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Registrarse") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(AppScreens.RegisterScreen.route)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Create,
                            contentDescription = "Registrarse"
                        )
                    }
                )

                Spacer(modifier = Modifier.weight(1f))
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Sección de configuración
                NavigationDrawerItem(
                    label = { Text("Configuración") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(AppScreens.SettingsScreen.route)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Configuración"
                        )
                    }
                )
            }
        },
        gesturesEnabled = true,
    ) {
        // Scaffold SOLO con la MainBottomBar
        Scaffold(
            bottomBar = {
                MainBottomBar(
                    navController = navController,
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    }
                )
            }
        ) { contentPadding ->
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(contentPadding)
            )
        }
    }
}
