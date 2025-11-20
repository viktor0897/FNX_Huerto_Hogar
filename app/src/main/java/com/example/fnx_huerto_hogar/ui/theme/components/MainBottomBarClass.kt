package com.example.fnx_huerto_hogar.ui.theme.components

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import com.example.fnx_huerto_hogar.navigate.AppScreens


enum class MainBottomBarClass(
    val route: String?,
    val icon: ImageVector,
    val label: String,
    val contentDescription: String
){
    //Por orden de izquierda a derecha:

    HOME(
        route = AppScreens.HomeScreen.route,
        icon = Icons.Outlined.Home,
        label = "Home",
        contentDescription = "Home"
    ),

    FAVORITOS(
        route = AppScreens.FavoriteScreen.route,
        icon = Icons.Outlined.FavoriteBorder,
        label = "Favoritos",
        contentDescription = "Favorite"
    ),

    CARRITO(
        route = AppScreens.CartScreen.route,
        icon = Icons.Outlined.ShoppingCart,
        label = "Carrito",
        contentDescription = "Cart"
    ),

    CATALOGO(
        route = AppScreens.CatalogScreen.route,
        icon = Icons.Outlined.Search,
        label = "Catálogo",
        contentDescription = "Catalog"
    ),

    MENU(
        route = null,
        icon = Icons.Outlined.Menu,
        label = "Menú",
        contentDescription = "Menu"
    )
}