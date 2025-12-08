package com.example.fnx_huerto_hogar.ui.theme.screen

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.fnx_huerto_hogar.data.repository.UserRepository
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CameraCaptureScreen(
    navController: NavController
) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // 1. Crear archivo para la foto
    val photoFile = remember { createImageFile(context) }

    // 2. Crear URI para el archivo
    val fileUri = remember(photoFile) {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
    }

    // 3. Launcher para tomar foto
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = fileUri // La foto se guardó en fileUri
        }
    }

    // 4. Launcher para permisos (¡ESTE ES EL QUE MUESTRA EL DIÁLOGO!)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Si el usuario DA PERMISO, abrir cámara
            cameraLauncher.launch(fileUri)
        } else {
            // Si el usuario NIEGA permiso, regresar
            navController.popBackStack()
        }
    }

    // 5. Al iniciar la pantalla, SOLICITAR PERMISO
    LaunchedEffect(Unit) {
        // Esto SÍ muestra el diálogo de permisos al usuario
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    // 6. Mostrar contenido según el estado
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Foto de Perfil",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (imageUri == null) {
            // Mientras esperamos permisos o foto
            CircularProgressIndicator()
            Text(
                text = "Esperando permisos de cámara...",
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "Por favor, acepta el permiso cuando aparezca",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            // Mostrar foto tomada
            Card(
                modifier = Modifier
                    .size(250.dp)
                    .padding(bottom = 24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "Foto capturada",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = "¡Foto tomada con éxito!",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "¿Usar esta foto para tu perfil?",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        imageUri?.let { uri ->
                            // GUARDAR LA FOTO - ¡ESTO ES LO IMPORTANTE!
                            // Convertir URI a String y guardar en Repository
                            UserRepository.updateProfilePicture(uri.toString())

                            // Regresar a la pantalla anterior
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Usar esta foto")
                }

                OutlinedButton(
                    onClick = {
                        // Tomar otra foto
                        imageUri = null
                        cameraLauncher.launch(fileUri)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Tomar otra")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}

// Función para crear archivo de imagen (MANTENER ESTA FUNCIÓN)
private fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    return File.createTempFile(
        "PROFILE_${timeStamp}_",
        ".jpg",
        storageDir
    )
}