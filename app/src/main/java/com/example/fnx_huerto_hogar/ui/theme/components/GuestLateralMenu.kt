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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fnx_huerto_hogar.R
import com.example.fnx_huerto_hogar.ui.theme.GrayBackground
import com.example.fnx_huerto_hogar.ui.theme.GreenSecondary

@Composable
fun GuestLateralMenu(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onSettingsClick: () -> Unit
){
    ModalDrawerSheet(
        drawerContainerColor = GrayBackground
    ){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(id = R.drawable.cabecero),
                contentDescription = "Logo Huerto Hogar",
                modifier = Modifier
                    .size(160.dp)
                    .padding(bottom = 2.dp),
                contentScale = ContentScale.Fit
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 4.dp),
            color = GreenSecondary
        )

        Spacer(modifier = Modifier.padding(vertical = 4.dp))

        //Sección de usuario (Invitado)
        NavigationDrawerItem(
            label = {
                Text(
                    text = "Iniciar Sesión",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            selected = false,
            onClick = onLoginClick,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "Iniciar Sesión",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        )

        NavigationDrawerItem(
            label = {
                Text(
                    text = "Registrarse",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            selected = false,
            onClick = onRegisterClick,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Create,
                    contentDescription = "Registrarse",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        )
    }
}