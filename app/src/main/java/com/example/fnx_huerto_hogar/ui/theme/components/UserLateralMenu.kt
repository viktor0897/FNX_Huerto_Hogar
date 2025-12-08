package com.example.fnx_huerto_hogar.ui.theme.components

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.fnx_huerto_hogar.R
import com.example.fnx_huerto_hogar.data.model.User
import com.example.fnx_huerto_hogar.ui.theme.BrownSecondary
import com.example.fnx_huerto_hogar.ui.theme.GrayBackground
import com.example.fnx_huerto_hogar.ui.theme.GreenPrimary
import com.example.fnx_huerto_hogar.ui.theme.GreenSecondary
import com.example.fnx_huerto_hogar.ui.theme.screen.*
import com.example.fnx_huerto_hogar.ui.theme.screen.CameraCaptureScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fnx_huerto_hogar.data.repository.UserRepository

@Composable
fun UserLateralMenu(
    currentUser: User,
    onLogoutClick: () -> Unit,
    onSettingsClick: () -> Unit,
    navController: NavController,
    onCameraClick: () -> Unit
) {
    // 1. Obtener la foto ACTUAL del Repository
    val currentPhotoUri = remember {
        // Leer la foto directamente del Repository
        UserRepository.getCurrentUser()?.profilePicture?.let { Uri.parse(it) }
    }

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
            // 2. Mostrar la foto (si existe) o la imagen por defecto
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clickable {
                        onCameraClick() // Esto navega a la pantalla de cámara
                    }
            ) {
                if (currentPhotoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = currentPhotoUri),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .align(Alignment.Center),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.cabecero),
                        contentDescription = "Foto de perfil por defecto",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .align(Alignment.Center),
                        contentScale = ContentScale.Crop
                    )
                }
                // Overlay circular para indicar que es clickeable
                Surface(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .align(Alignment.Center),
                    color = Color.Black.copy(alpha = 0.3f),
                    shape = CircleShape
                ) {}

                // Icono de cámara en el centro
                Icon(
                    imageVector = Icons.Filled.AddAPhoto,
                    contentDescription = "Cambiar foto",
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.Center)
                )

                // Texto "Cambiar foto" en la parte inferior
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .align(Alignment.BottomCenter),
                    color = GreenPrimary.copy(alpha = 0.8f),
                    shape = CircleShape
                ) {
                    Text(
                        text = "Cambiar foto",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.padding(vertical = 16.dp))

            // Información del usuario
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