package com.example.fnx_huerto_hogar.ui.theme.screen

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fnx_huerto_hogar.data.DeliveryType
import com.example.fnx_huerto_hogar.data.PaymentMethod
import com.example.fnx_huerto_hogar.ui.theme.*
import com.example.fnx_huerto_hogar.ui.theme.viewModel.CheckoutViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    usuarioId: Long = 1L // Pasar el ID del usuario logueado
) {
    val viewModel: CheckoutViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()
    val totalItems by viewModel.totalItems.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Control para mostrar el diálogo del clima
    var showWeatherDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Finalizar Compra",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Volver",
                            tint = GreenPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = GrayBackground
                )
            )
        },
        containerColor = GrayBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Resumen del pedido (simplificado)
            OrderSummaryCard(
                totalItems = totalItems,
                totalPrice = totalPrice
            )

            // Tipo de entrega
            DeliveryTypeSection(
                deliveryType = state.deliveryType,
                onDeliveryTypeSelected = viewModel::setDeliveryType
            )

            // Dirección de entrega (solo si es delivery)
            if (state.deliveryType == DeliveryType.HOME_DELIVERY) {
                DeliveryAddressSection(
                    address = state.deliveryAddress,
                    onAddressChange = viewModel::updateDeliveryAddress
                )
            } else {
                StorePickupSection(
                    viewModel = viewModel,
                    navController = navController
                )
            }

            // Datos del destinatario
            RecipientInfoSection(
                name = state.recipientName,
                phone = state.recipientPhone,
                onNameChange = { name ->
                    viewModel.updateRecipientInfo(name, state.recipientPhone)
                },
                onPhoneChange = { phone ->
                    viewModel.updateRecipientInfo(state.recipientName, phone)
                }
            )

            // Método de pago
            PaymentMethodSection(
                paymentMethod = state.paymentMethod,
                onPaymentMethodSelected = viewModel::setPaymentMethod
            )

            // Instrucciones de entrega (opcional)
            DeliveryInstructionsSection(
                instructions = state.deliveryInstruction,
                onInstructionsChange = viewModel::updateDeliveryInstruction
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Confirmar
            ConfirmOrderButton(
                isLoading = state.isLoading,
                isConfirmed = state.isConfirmed,
                onConfirm = {
                    viewModel.confirmOrder(
                        onWeatherReady = { showWeatherDialog = true }
                    )
                }
            )

            // Mostramos error si existe
            if (state.errorMessage.isNotEmpty()) {
                ErrorMessage(
                    message = state.errorMessage,
                    onDismiss = viewModel::clearError
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Diálogo del clima
        if (showWeatherDialog) {
            WeatherAlertDialog(
                weatherDescription = state.weatherDescription,
                weatherTemperature = state.weatherTemperature,
                onDismiss = { showWeatherDialog = false }
            )
        }
    }
}

@Composable
fun OrderSummaryCard(
    totalItems: Int,
    totalPrice: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Resumen del pedido",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total de productos:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$totalItems items",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = GreenSecondary.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = GreenPrimary
                )
                Box(
                    modifier = Modifier
                        .background(
                            color = YellowAccent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "$${String.format("%.2f", totalPrice)}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun DeliveryTypeSection(
    deliveryType: DeliveryType,
    onDeliveryTypeSelected: (DeliveryType) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Tipo de entrega",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            DeliveryTypeOption(
                title = "Despacho a Domicilio",
                description = "Recibe el pedido en tu dirección",
                icon = Icons.Outlined.LocalShipping,
                isSelected = deliveryType == DeliveryType.HOME_DELIVERY,
                onClick = { onDeliveryTypeSelected(DeliveryType.HOME_DELIVERY) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            DeliveryTypeOption(
                title = "Retiro en tienda",
                description = "Retira el pedido en Tienda",
                icon = Icons.Outlined.Store,
                isSelected = deliveryType == DeliveryType.STORE_PICKUP,
                onClick = { onDeliveryTypeSelected(DeliveryType.STORE_PICKUP) }
            )
        }
    }
}

@Composable
fun DeliveryTypeOption(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) GreenSecondary else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 2.dp else 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isSelected) GreenPrimary else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = if (isSelected) GreenPrimary else Color.Black
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isSelected) GreenPrimary else Color.Gray
                    )
                }
            }
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = GreenPrimary,
                    unselectedColor = Color.Gray
                )
            )
        }
    }
}

@Composable
fun DeliveryAddressSection(
    address: String,
    onAddressChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Dirección",
                    tint = GreenPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Dirección de Entrega",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = GreenPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = address,
                onValueChange = onAddressChange,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color.LightGray
                ),
                placeholder = {
                    Text("Ingresa tu dirección completa", color = Color.Gray)
                },
                singleLine = false,
                minLines = 2,
                maxLines = 3
            )
        }
    }
}

@Composable
fun StorePickupSection(
    viewModel: CheckoutViewModel,
    navController: NavController
) {
    var showMap by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Store,
                    contentDescription = "Sucursal",
                    tint = GreenPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sucursal de Retiro",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = GreenPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Sucursal Central\nAv. Principal 123, Santiago\nHorario: Lunes a Viernes 9:00 - 19:00",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showMap = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenSecondary
                )
            ) {
                Text("Seleccionar punto de retiro en mapa", color = GreenPrimary)
            }

            viewModel.pickupLocation?.let { location ->
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = GreenSecondary.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.CheckCircle,
                            contentDescription = "Ubicación seleccionada",
                            tint = GreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Ubicación personalizada seleccionada",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = GreenPrimary
                            )
                            Text(
                                text = "Lat: ${"%.6f".format(location.latitude)}, Lng: ${"%.6f".format(location.longitude)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }

    if (showMap) {
        AlertDialog(
            onDismissRequest = { showMap = false },
            title = {},
            text = {
                MapCard(
                    modifier = Modifier.fillMaxWidth(),
                    onLocationSelected = { location ->
                        viewModel.savePickupLocation(location)
                    },
                    onCancel = { showMap = false }
                )
            },
            confirmButton = {},
            dismissButton = {}
        )
    }
}

@Composable
fun RecipientInfoSection(
    name: String,
    phone: String,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Datos del Destinatario",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color.LightGray
                ),
                placeholder = { Text("Nombre completo", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Outlined.Person, null, tint = GreenPrimary) }
            )

            OutlinedTextField(
                value = phone,
                onValueChange = onPhoneChange,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color.LightGray
                ),
                placeholder = { Text("Teléfono de contacto", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Outlined.Phone, null, tint = GreenPrimary) }
            )
        }
    }
}

@Composable
fun PaymentMethodSection(
    paymentMethod: PaymentMethod,
    onPaymentMethodSelected: (PaymentMethod) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Método de Pago",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            PaymentMethodOption(
                title = "Tarjeta de Crédito",
                icon = Icons.Outlined.CreditCard,
                isSelected = paymentMethod == PaymentMethod.CREDIT_CARD,
                onClick = { onPaymentMethodSelected(PaymentMethod.CREDIT_CARD) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            PaymentMethodOption(
                title = "Tarjeta de Débito",
                icon = Icons.Outlined.CreditCard,
                isSelected = paymentMethod == PaymentMethod.DEBIT_CARD,
                onClick = { onPaymentMethodSelected(PaymentMethod.DEBIT_CARD) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            PaymentMethodOption(
                title = "PayPal",
                icon = Icons.Outlined.Payment,
                isSelected = paymentMethod == PaymentMethod.PAYPAL,
                onClick = { onPaymentMethodSelected(PaymentMethod.PAYPAL) }
            )
        }
    }
}

@Composable
fun PaymentMethodOption(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) GreenSecondary else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isSelected) GreenPrimary else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) GreenPrimary else Color.Black,
                    modifier = Modifier.weight(1f)
                )
            }
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = GreenPrimary,
                    unselectedColor = Color.Gray
                )
            )
        }
    }
}

@Composable
fun DeliveryInstructionsSection(
    instructions: String,
    onInstructionsChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notes,
                    contentDescription = "Instrucciones",
                    tint = GreenPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Instrucciones de Entrega (Opcional)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = GreenPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = instructions,
                onValueChange = onInstructionsChange,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color.LightGray
                ),
                placeholder = {
                    Text("Ej: Llamar antes de llegar, timbre rojo, etc.", color = Color.Gray)
                },
                singleLine = false,
                minLines = 2,
                maxLines = 3
            )
        }
    }
}

@Composable
fun ConfirmOrderButton(
    isLoading: Boolean,
    isConfirmed: Boolean,
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    color = GreenPrimary,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Procesando pago...", color = GreenPrimary)
            }
            isConfirmed -> {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = "Confirmado",
                    tint = GreenPrimary,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "¡Pedido Confirmado!",
                    style = MaterialTheme.typography.titleMedium,
                    color = GreenPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
            else -> {
                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = "Confirmar Pedido",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Text(
            text = "Al confirmar, aceptas nuestros términos y condiciones",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}

@Composable
fun ErrorMessage(
    message: String,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE5E5))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Error,
                contentDescription = "Error",
                tint = Color.Red,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Red,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Cerrar",
                    tint = Color.Red
                )
            }
        }
    }
}

// Mantener MapCard y WeatherAlertDialog igual que antes
@SuppressLint("MissingPermission")
@Composable
fun MapCard(
    modifier: Modifier = Modifier,
    onLocationSelected: (LatLng) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var locationMessage by remember { mutableStateOf("Inicializando...") }
    var isLoading by remember { mutableStateOf(false) }
    var hasLocationPermission by remember { mutableStateOf(false) }

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    suspend fun getCurrentLocation() {
        if (!hasLocationPermission) return
        isLoading = true
        try {
            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY, null
            ).await()
            if (location != null) {
                userLocation = LatLng(location.latitude, location.longitude)
                selectedLocation = userLocation // Inicializar selección con ubicación actual
                locationMessage = "Ubicación obtenida"
            } else {
                locationMessage = "No se pudo obtener ubicación"
            }
        } catch (e: Exception) {
            locationMessage = "Error: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val hasFineLocation = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val hasCoarseLocation = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            hasLocationPermission = hasFineLocation || hasCoarseLocation
            if (hasLocationPermission) {
                locationMessage = "Permisos concedidos"
                coroutineScope.launch { getCurrentLocation() }
            } else {
                locationMessage = "Permisos denegados"
            }
        }
    )

    // Solicitar permisos al iniciar
    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(-33.4489, -70.6693), // Santiago por defecto
            12f
        )
    }

    LaunchedEffect(userLocation) {
        userLocation?.let { location ->
            cameraPositionState.position = CameraPosition.fromLatLngZoom(location, 15f)
        }
    }

    Column(modifier = modifier) {
        // Indicador de carga o mensaje
        if (isLoading) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = GreenPrimary,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Obteniendo ubicación...",
                    style = MaterialTheme.typography.bodySmall,
                    color = GreenPrimary
                )
            }
        } else if (locationMessage.isNotEmpty() && !hasLocationPermission) {
            Text(
                text = locationMessage,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Mapa
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = hasLocationPermission
                ),
                uiSettings = MapUiSettings(
                    myLocationButtonEnabled = true,
                    zoomControlsEnabled = true
                ),
                onMapClick = { latLng ->
                    selectedLocation = latLng
                }
            ) {
                // Marcador de ubicación seleccionada
                selectedLocation?.let { location ->
                    Marker(
                        state = MarkerState(position = location),
                        title = "Punto de retiro",
                        snippet = "Lat: ${"%.6f".format(location.latitude)}, Lng: ${"%.6f".format(location.longitude)}"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Información de ubicación seleccionada
        selectedLocation?.let { location ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = GreenSecondary.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "📍 Ubicación seleccionada",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = GreenPrimary
                    )
                    Text(
                        text = "Lat: ${"%.6f".format(location.latitude)}, Lng: ${"%.6f".format(location.longitude)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                border = ButtonDefaults.outlinedButtonBorder(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cancelar", color = GreenPrimary)
            }

            Button(
                onClick = {
                    selectedLocation?.let { location ->
                        onLocationSelected(location)
                        onCancel()
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = selectedLocation != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenPrimary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Confirmar")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAlertDialog(
    weatherDescription: String,
    weatherTemperature: Double,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "🌤️ Clima en tu ubicación",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary
            )
        },
        text = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Condiciones:",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Text(
                            text = weatherDescription.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                color = YellowAccent.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "${weatherTemperature.toInt()}°C",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Recomendación simple
                val recommendation = when {
                    weatherTemperature < 10 -> "❄️ Abrígate bien para el retiro"
                    weatherTemperature < 20 -> "🧥 Lleva una chaqueta ligera"
                    weatherTemperature < 30 -> "😎 Temperatura ideal para salir"
                    else -> "🔥 Hidrátate bien, hace calor"
                }

                Text(
                    text = recommendation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = GreenPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenPrimary
                )
            ) {
                Text("Continuar con el pedido")
            }
        }
    )
}