package com.example.fnx_huerto_hogar.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fnx_huerto_hogar.R
import com.example.fnx_huerto_hogar.data.model.Usuario
import com.example.fnx_huerto_hogar.ui.theme.BrownSecondary
import com.example.fnx_huerto_hogar.ui.theme.GrayBackground
import com.example.fnx_huerto_hogar.ui.theme.GreenPrimary
import com.example.fnx_huerto_hogar.ui.theme.GreenSecondary

@Composable
fun UserLateralMenu(
    currentUser: Usuario,
    onLogoutClick: () -> Unit,
    onSettingsClick: () -> Unit,
    navController: NavController
) {


    ModalDrawerSheet(
        drawerContainerColor = GrayBackground,
        modifier = Modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar simple
            Image(
                painter = painterResource(id = R.drawable.cabecero),
                contentDescription = "Avatar del usuario",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.padding(vertical = 16.dp))

            // Información del usuario
            Text(
                text = "${currentUser.nombre} ${currentUser.apellido}",
                style = MaterialTheme.typography.titleLarge,
                color = GreenPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = currentUser.email,
                style = MaterialTheme.typography.bodyMedium,
                color = BrownSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "${currentUser.direccion}, ${currentUser.comuna}",
                style = MaterialTheme.typography.bodySmall,
                color = BrownSecondary,
                textAlign = TextAlign.Center
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 4.dp),
            color = GreenSecondary
        )

        Spacer(modifier = Modifier.padding(vertical = 4.dp))

        Spacer(modifier = Modifier.weight(1f))

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = GreenSecondary
        )

        // Sección de configuración
        NavigationDrawerItem(
            label = {
                Text(
                    text = "Configuración",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            selected = false,
            onClick = onSettingsClick,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Configuración",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = GreenSecondary
        )

        // Opción para cerrar sesión
        NavigationDrawerItem(
            label = {
                Text(
                    text = "Cerrar Sesión",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            selected = false,
            onClick = onLogoutClick,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.ExitToApp,
                    contentDescription = "Cerrar Sesión",
                    tint = Color.Red
                )
            },
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}