package com.example.fnx_huerto_hogar.ui.theme.screen

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

@SuppressLint("MissingPermission")
@Composable
fun GeolocalizationScreen(){

    //Definir el contexto dónde está ubicado el cliente
    val context = LocalContext.current

    //Generar el cliente de ubicación
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    //Generar objeto que recuperará (Latitud y Longitud)
    var userLocation by remember { mutableStateOf<Pair<Double, Double>?>(null) }

    //Texto que presentará mensajes
    var locationMessage by remember { mutableStateOf("Buscando Ubicación") }

    //Función que permite llamar a la ubicación
    suspend fun getCurrentLocation(){
        try {
            //Recuperamos la ubicación
            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY, null
            ).await()
            //Saber si la ubicación no es null
            if (location !=null){
                userLocation = Pair(location.latitude, location.longitude)
                locationMessage = "Ubicación Recuperada"
            }else{
                locationMessage = "Problemas con la recuperación de ubicación"
            }
        }catch (e: Exception){
            locationMessage = "Error: ${e.message}"
        }
    }

    //Lanzar el mensaje de pedir permisos de ubicación
    val locationPermissionLaunch = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                locationMessage = "Permisos de ubicación otorgados"
            } else {
                locationMessage = "Permisos de ubicación NO otorgados"
            }
        }
    )


}