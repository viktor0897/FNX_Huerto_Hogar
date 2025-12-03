package com.example.fnx_huerto_hogar.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fnx_huerto_hogar.R
import com.example.fnx_huerto_hogar.data.model.User
import com.example.fnx_huerto_hogar.ui.theme.BrownSecondary
import com.example.fnx_huerto_hogar.ui.theme.GrayBackground
import com.example.fnx_huerto_hogar.ui.theme.GreenPrimary
import com.example.fnx_huerto_hogar.ui.theme.GreenSecondary

@Composable
fun UserLateralMenu(
    currentUser: User,
    onLogoutClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = GrayBackground
    ) {
        // Encabezado con foto circular y información centrada
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Círculo para la foto
            Image(
                painter = painterResource(id = R.drawable.cabecero), // Puedes cambiar por una foto de perfil
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.padding(vertical = 16.dp))

            // Información del usuario centrada
            Text(
                text = "${currentUser.name} ${currentUser.lastName}",
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
                text = "${currentUser.address}, ${currentUser.comuna}",
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
            }
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
            }
        )
    }
}