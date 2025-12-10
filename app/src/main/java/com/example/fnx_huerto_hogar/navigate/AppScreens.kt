package com.example.fnx_huerto_hogar.navigate

// En AppScreens.kt (si lo tienes)
sealed class AppScreens(val route: String) {
    object HomeScreen : AppScreens("home_screen")
    object CatalogScreen : AppScreens("catalog_screen")
    object CartScreen : AppScreens("cart_screen")
    object FavoriteScreen : AppScreens("favorite_screen")
    object LoginScreen : AppScreens("login_screen")
    object RegisterScreen : AppScreens("register_screen")
    object SettingsScreen : AppScreens("settings_screen")
    object CameraCaptureScreen : AppScreens(route = "camera_screen")
    object GeolocalizationScreen : AppScreens(route = "geolocalization_screen")
    object CheckoutScreen : AppScreens(route = "checkout_screen")

}