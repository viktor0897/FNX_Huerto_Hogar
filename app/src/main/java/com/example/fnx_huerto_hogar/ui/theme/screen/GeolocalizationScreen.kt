package com.example.fnx_huerto_hogar.ui.theme.screen

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@SuppressLint("MissingPermission", "CoroutineCreationDuringComposition")
@Composable
fun GeolocalizationScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Estados
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var locationMessage by remember { mutableStateOf("Inicializando...") }
    var isLoading by remember { mutableStateOf(false) }
    var hasLocationPermission by remember { mutableStateOf(false) }

    // Cliente de ubicación
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // Función para obtener ubicación
    suspend fun getCurrentLocation() {
        if (!hasLocationPermission) return

        isLoading = true
        try {
            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY, null
            ).await()

            if (location != null) {
                userLocation = LatLng(location.latitude, location.longitude)
                locationMessage = "Ubicación obtenida: ${"%.6f".format(location.latitude)}, ${"%.6f".format(location.longitude)}"
            } else {
                locationMessage = "No se pudo obtener la ubicación"
            }
        } catch (e: Exception) {
            locationMessage = "Error: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }

    // Permisos de ubicación
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val hasFineLocation = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val hasCoarseLocation = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            hasLocationPermission = hasFineLocation || hasCoarseLocation

            when {
                hasFineLocation -> {
                    locationMessage = "Permisos precisos concedidos"
                    coroutineScope.launch {
                        getCurrentLocation()
                    }
                }
                hasCoarseLocation -> {
                    locationMessage = "Permisos aproximados concedidos"
                    coroutineScope.launch {
                        getCurrentLocation()
                    }
                }
                else -> {
                    locationMessage = "Permisos de ubicación denegados"
                    hasLocationPermission = false
                }
            }
        }
    )

    // Estado de la cámara del mapa
    val cameraPositionState = rememberCameraPositionState()

    // Actualizar cámara cuando cambia la ubicación
    LaunchedEffect(userLocation) {
        userLocation?.let { location ->
            cameraPositionState.position = CameraPosition.fromLatLngZoom(location, 15f)
        }
    }

    // Solicitar permisos al inicio
    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Mapa o placeholder
        if (userLocation != null && hasLocationPermission) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = true,
                    mapType = com.google.maps.android.compose.MapType.NORMAL
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    compassEnabled = true,
                    myLocationButtonEnabled = true,
                    zoomGesturesEnabled = true,
                    scrollGesturesEnabled = true,
                    rotationGesturesEnabled = true,
                    tiltGesturesEnabled = true
                )
            ) {
                userLocation?.let { location ->
                    Marker(
                        state = MarkerState(position = location),
                        title = "Tu ubicación",
                        snippet = "Lat: ${"%.6f".format(location.latitude)}\nLng: ${"%.6f".format(location.longitude)}"
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Obteniendo ubicación...")
                } else if (!hasLocationPermission) {
                    Text("Esperando permisos de ubicación...")
                } else {
                    Text("Obteniendo datos del mapa...")
                }
            }
        }

        // Mensaje de estado
        Text(
            text = locationMessage,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Botones de control
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Solicitar Permisos de Ubicación")
            }

            Button(
                onClick = {
                    if (hasLocationPermission) {
                        coroutineScope.launch {
                            getCurrentLocation()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = hasLocationPermission && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Obteniendo...")
                } else {
                    Text("Actualizar Ubicación")
                }
            }
        }

        // Mostrar coordenadas
        userLocation?.let { location ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Latitud: ${"%.6f".format(location.latitude)}")
                Text("Longitud: ${"%.6f".format(location.longitude)}")
            }
        }
    }
}