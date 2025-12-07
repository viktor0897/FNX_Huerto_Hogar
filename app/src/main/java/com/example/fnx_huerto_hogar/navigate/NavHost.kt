package com.example.fnx_huerto_hogar.navigate

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fnx_huerto_hogar.ui.theme.screen.CameraCaptureScreen
import com.example.fnx_huerto_hogar.ui.theme.screen.CartScreen
import com.example.fnx_huerto_hogar.ui.theme.screen.CatalogScreen
import com.example.fnx_huerto_hogar.ui.theme.screen.FavoriteScreen
import com.example.fnx_huerto_hogar.ui.theme.screen.HomeScreen
import com.example.fnx_huerto_hogar.ui.theme.screen.LoginScreen
import com.example.fnx_huerto_hogar.ui.theme.screen.RegisterScreen
import com.example.fnx_huerto_hogar.ui.theme.screen.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppScreens.HomeScreen.route,
        modifier = modifier
    ) {
        composable(route = AppScreens.HomeScreen.route) {
            HomeScreen(navController = navController)
        }

        composable(route = AppScreens.CatalogScreen.route) {
            CatalogScreen(navController = navController)
        }

        composable(route = AppScreens.CartScreen.route) {
            CartScreen(navController = navController)
        }

        composable(route = AppScreens.FavoriteScreen.route) {
            FavoriteScreen(navController = navController)
        }

        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(navController = navController)
        }

        composable(route = AppScreens.RegisterScreen.route) {
            RegisterScreen(navController = navController)
        }

        composable(route = AppScreens.SettingsScreen.route) {
            SettingsScreen(navController = navController)
        }

        composable(route = AppScreens.CameraCaptureScreen.route){
            CameraCaptureScreen(navController = navController)
        }
    }
}