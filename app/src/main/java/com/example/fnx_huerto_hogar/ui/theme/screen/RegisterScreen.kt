package com.example.fnx_huerto_hogar.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fnx_huerto_hogar.navigate.AppScreens
import com.example.fnx_huerto_hogar.ui.theme.GrayBackground
import com.example.fnx_huerto_hogar.ui.theme.GreenPrimary
import com.example.fnx_huerto_hogar.ui.theme.viewModel.RegisterViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: RegisterViewModel = viewModel()
) {
    // Observar los estados del ViewModel
    val name by viewModel.name.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val address by viewModel.address.collectAsState()
    val comuna by viewModel.comuna.collectAsState()
    val region by viewModel.region.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    // Estados locales para los dropdowns (simplificado)
    var expandedRegion by remember { mutableStateOf(false) }
    var expandedComuna by remember { mutableStateOf(false) }

    // Navegar si el registro fue exitoso
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            delay(1500)
            navController.navigate(AppScreens.HomeScreen.route) {
                popUpTo(AppScreens.RegisterScreen.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            // Título
            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Únete a nuestra comunidad",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Campos del formulario
            CamposRegistro(
                name = name,
                onNameChange = viewModel::onNameChange,
                lastName = lastName,
                onLastNameChange = viewModel::onLastNameChange,
                email = email,
                onEmailChange = viewModel::onEmailChange,
                password = password,
                onPasswordChange = viewModel::onPasswordChange,
                confirmPassword = confirmPassword,
                onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
                phone = phone,
                onPhoneChange = viewModel::onPhoneChange,
                address = address,
                onAddressChange = viewModel::onAdressChange,
                comuna = comuna,
                onComunaChange = viewModel::onComunaChange,
                region = region,
                onRegionChange = viewModel::onRegionChange,
                expandedRegion = expandedRegion,
                expandedComuna = expandedComuna,
                onRegionExpandedChange = { expandedRegion = it },
                onComunaExpandedChange = { expandedComuna = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (isSuccess) {
                Text(
                    text = "Registro Exitoso",
                    color = GreenPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .background(
                            color = GreenPrimary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Mostrar error si existe
            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }

            // Texto informativo
            Text(
                text = "Al registrarte, aceptas nuestros términos y condiciones",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Registro
            Button(
                onClick = {
                    viewModel.register()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading && !isSuccess,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Registrando")
                    }
                } else if (isSuccess) {
                    Text("Registrado")
                } else {
                    Text(
                        text = "Registrarse",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Para ir al login si ya se tiene cuenta
            if (isSuccess) {
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(
                    onClick = {
                        navController.navigate(AppScreens.LoginScreen.route) {
                            popUpTo(AppScreens.RegisterScreen.route) { inclusive = true }
                        }
                    }
                ) {
                    Text(
                        text = "¿Ya tienes cuenta? Inicia Sesión",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GreenPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CamposRegistro(
    name: String,
    onNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    address: String,
    onAddressChange: (String) -> Unit,
    comuna: String,
    onComunaChange: (String) -> Unit,
    region: String,
    onRegionChange: (String) -> Unit,
    expandedRegion: Boolean,
    expandedComuna: Boolean,
    onRegionExpandedChange: (Boolean) -> Unit,
    onComunaExpandedChange: (Boolean) -> Unit
) {
    // Obtener las listas directamente del objeto ChileLocations
    val regions = com.example.fnx_huerto_hogar.data.ChileLocations.regions
    val communes = com.example.fnx_huerto_hogar.data.ChileLocations.communesByRegion[region] ?: emptyList()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Fila para nombre y apellido
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo Nombre
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Nombre") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = "Nombre", tint = GreenPrimary)
                },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    focusedLabelColor = GreenPrimary,
                    cursorColor = GreenPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            )

            // Campo Apellido
            OutlinedTextField(
                value = lastName,
                onValueChange = onLastNameChange,
                label = { Text("Apellido") },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    focusedLabelColor = GreenPrimary,
                    cursorColor = GreenPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Campo Email
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Correo electrónico") },
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = "Email", tint = GreenPrimary)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                focusedLabelColor = GreenPrimary,
                cursorColor = GreenPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        )

        // Campo Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Contraseña") },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "Contraseña", tint = GreenPrimary)
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                focusedLabelColor = GreenPrimary,
                cursorColor = GreenPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        )

        // Campo Confirmar Contraseña
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = { Text("Confirmar Contraseña") },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "Confirmar Contraseña", tint = GreenPrimary)
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                focusedLabelColor = GreenPrimary,
                cursorColor = GreenPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        )

        // Campo Teléfono
        OutlinedTextField(
            value = phone,
            onValueChange = onPhoneChange,
            label = { Text("Teléfono") },
            leadingIcon = {
                Icon(Icons.Default.Phone, contentDescription = "Teléfono", tint = GreenPrimary)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                focusedLabelColor = GreenPrimary,
                cursorColor = GreenPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        )

        // Campo Dirección
        OutlinedTextField(
            value = address,
            onValueChange = onAddressChange,
            label = { Text("Dirección") },
            leadingIcon = {
                Icon(Icons.Default.Home, contentDescription = "Dirección", tint = GreenPrimary)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                focusedLabelColor = GreenPrimary,
                cursorColor = GreenPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        )

        // Fila para región y comuna con dropdowns
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Dropdown para Región
            Box(
                modifier = Modifier.weight(1f)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expandedRegion,
                    onExpandedChange = onRegionExpandedChange
                ) {
                    OutlinedTextField(
                        value = region,
                        onValueChange = { },
                        label = { Text("Región *") },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRegion)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GreenPrimary,
                            focusedLabelColor = GreenPrimary,
                            cursorColor = GreenPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = expandedRegion,
                        onDismissRequest = { onRegionExpandedChange(false) }
                    ) {
                        if (regions.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text(regionOption) },
                                onClick = {
                                    onRegionChange(regionOption)
                                    onRegionExpandedChange(false)
                                }
                            )
                        } else {
                            regions.forEach { regionOption ->
                                DropdownMenuItem(
                                    text = { Text(regionOption) },
                                    onClick = {
                                        onRegionChange(regionOption)
                                        onRegionDropdownToggle(false)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Dropdown para Comuna
            Box(
                modifier = Modifier.weight(1f)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expandedComuna,
                    onExpandedChange = { newValue ->
                        if (region.isNotEmpty()) {
                            onComunaExpandedChange(newValue)
                        }
                    }
                ) {
                    OutlinedTextField(
                        value = comuna,
                        onValueChange = { },
                        label = { Text("Comuna *") },
                        readOnly = true,
                        enabled = region.isNotEmpty(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedComuna)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (region.isNotEmpty()) GreenPrimary else Color.Gray,
                            focusedLabelColor = if (region.isNotEmpty()) GreenPrimary else Color.Gray,
                            cursorColor = if (region.isNotEmpty()) GreenPrimary else Color.Gray,
                            disabledBorderColor = Color.Gray,
                            disabledLabelColor = Color.Gray,
                            disabledTextColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    if (region.isNotEmpty() && expandedComuna) {
                        ExposedDropdownMenu(
                            expanded = expandedComuna,
                            onDismissRequest = { onComunaExpandedChange(false) }
                        ) {
                            if (communes.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text(communeOption) },
                                    onClick = {
                                        onComunaChange(communeOption)
                                        onComunaExpandedChange(false)
                                    }
                                )
                            } else {
                                communes.forEach { communeOption ->
                                    DropdownMenuItem(
                                        text = { Text(communeOption) },
                                        onClick = {
                                            onComunaChange(communeOption)
                                            onCommuneDropdownToggle(false)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}