package com.example.fnx_huerto_hogar.navigate

sealed class AppScreens(val route: String) {
    object HomeScreen: AppScreens(route = "home_screen")
    object CartScreen: AppScreens(route = "cart_screen")
    object CatalogScreen: AppScreens(route = "catalog_screen")
    object FavoriteScreen: AppScreens(route = "favorite_screen")
    object LoginScreen: AppScreens(route = "login_screen")
    object RegisterScreen: AppScreens(route = "register_screen")
    object SettingsScreen: AppScreens(route = "settings_screen")

}