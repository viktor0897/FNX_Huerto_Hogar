package com.example.fnx_huerto_hogar.ui.theme.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun MainBottomBar(
    navController: NavHostController,
    onMenuClick: () -> Unit
){
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    BottomAppBar {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val bottomBarItems = listOf(
                MainBottomBarClass.HOME,
                MainBottomBarClass.CATALOGO,
                MainBottomBarClass.CARRITO,
                MainBottomBarClass.FAVORITOS,
                MainBottomBarClass.MENU
            )

            bottomBarItems.forEach {destination ->
                val isSelected = currentRoute == destination.route

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (destination.route == null){
                            when(destination){
                                MainBottomBarClass.MENU -> onMenuClick()
                                else -> {}
                            }
                        }else {
                            destination.route?.let { route -> navController.navigate(route) }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = destination.contentDescription
                        )
                    },
                    label = {
                        Text(destination.label)
                    }
                )
            }
        }
    }
}
