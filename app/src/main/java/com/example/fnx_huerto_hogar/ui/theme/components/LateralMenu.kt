package com.example.fnx_huerto_hogar.ui.theme.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.fnx_huerto_hogar.data.repository.UserRepository
import com.example.fnx_huerto_hogar.navigate.AppScreens
import com.example.fnx_huerto_hogar.navigate.AppNavHost
import kotlinx.coroutines.launch

@Composable
fun LateralMenu() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    var currentUser by remember { mutableStateOf(UserRepository.getCurrentUser()) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            LaunchedEffect(drawerState.currentValue) {
                if (drawerState.isOpen){
                    currentUser = UserRepository.getCurrentUser()
                }
            }
            if (currentUser != null) {
                UserLateralMenu(
                    currentUser = currentUser!!,
                    onLogoutClick = {
                        scope.launch {
                            drawerState.close()
                            UserRepository.logout()
                            currentUser = null
                            navController.navigate(AppScreens.HomeScreen.route) {
                                popUpTo(0)
                            }
                        }
                    },
                    onSettingsClick = {
                        scope.launch {
                            drawerState.close()
                            navController.navigate(AppScreens.SettingsScreen.route)
                        }
                    },
                    navController = navController,

                    onCameraClick = {
                        scope.launch {
                            drawerState.close()
                            navController.navigate("camera_screen")
                        }
                    }
                )
            } else {
                GuestLateralMenu(
                    onLoginClick = {
                        scope.launch {
                            drawerState.close()
                            navController.navigate(AppScreens.LoginScreen.route)
                        }
                    },
                    onRegisterClick = {
                        scope.launch {
                            drawerState.close()
                            navController.navigate(AppScreens.RegisterScreen.route)
                        }
                    },
                    onSettingsClick = {
                        scope.launch {
                            drawerState.close()
                            navController.navigate(AppScreens.SettingsScreen.route)
                        }
                    }
                )
            }
        },
        gesturesEnabled = true,
    ) {
        Scaffold(
            bottomBar = {
                MainBottomBar(
                    navController = navController,
                    onMenuClick = {
                        scope.launch {
                            currentUser = UserRepository.getCurrentUser()
                            drawerState.open()
                        }
                    }
                )
            }
        ) { contentPadding ->
            AppNavHost(
                navController = navController,
                modifier = Modifier
                    .padding(contentPadding)
            )
        }
    }
}