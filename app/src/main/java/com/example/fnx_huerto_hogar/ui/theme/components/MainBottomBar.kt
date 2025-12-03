package com.example.fnx_huerto_hogar.ui.theme.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fnx_huerto_hogar.ui.theme.BrownLight
import com.example.fnx_huerto_hogar.ui.theme.BrownSecondary
import com.example.fnx_huerto_hogar.ui.theme.GrayBackground
import com.example.fnx_huerto_hogar.ui.theme.GreenPrimary
import com.example.fnx_huerto_hogar.ui.theme.GreenSecondary

@Composable
fun MainBottomBar(
    navController: NavHostController,
    onMenuClick: () -> Unit
){
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    BottomAppBar(
        containerColor = GrayBackground
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val bottomBarItems = listOf(
                MainBottomBarClass.MENU,
                MainBottomBarClass.CATALOGO,
                MainBottomBarClass.HOME,
                MainBottomBarClass.CARRITO,
                MainBottomBarClass.FAVORITOS
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
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GreenPrimary,
                        selectedTextColor = GreenPrimary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                        indicatorColor = GreenSecondary.copy(alpha = 0.3f)
                    )
                )
            }
        }
    }
}
