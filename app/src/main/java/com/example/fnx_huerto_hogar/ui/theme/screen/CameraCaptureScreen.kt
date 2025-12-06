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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CameraCaptureScreen(
    onPhotoTaken: (Uri) -> Unit,
    onCancel: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var hasCameraPermission by remember { mutableStateOf(false) }
    var imageCaptureUri by remember { mutableStateOf<Uri?>(null) }

    // Crear archivo temporal para la foto
    val photoFile = remember { createImageFile(context) }

    val fileUri = remember(photoFile) {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
    }

    // Launcher para cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageCaptureUri = fileUri
        }
    }

    // Launcher para permisos de cámara
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (isGranted) {
            cameraLauncher.launch(fileUri)
        } else {
            onCancel()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "Foto de Perfil",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Vista previa SI hay foto
        if (imageCaptureUri != null) {
            Card(
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageCaptureUri),
                    contentDescription = "Foto capturada",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Text(
                text = "¡Foto tomada con éxito!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Botones para aceptar/cancelar
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        imageCaptureUri?.let { uri ->
                            onPhotoTaken(uri)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Usar esta foto")
                }

                OutlinedButton(
                    onClick = {
                        imageCaptureUri = null
                        cameraLauncher.launch(fileUri)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Tomar otra")
                }
            }
        } else {
            // Si no hay foto, mostrar instrucciones
            Text(
                text = "Presiona el botón para tomar foto",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 32.dp)
                    .fillMaxWidth()
            )

            // Botón para tomar foto
            Button(
                onClick = {
                    if (hasCameraPermission) {
                        cameraLauncher.launch(fileUri)
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Tomar foto",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Tomar foto")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para cancelar
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}

// Función para crear archivo de imagen
private fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    return File.createTempFile(
        "PROFILE_${timeStamp}_",
        ".jpg",
        storageDir
    )
}