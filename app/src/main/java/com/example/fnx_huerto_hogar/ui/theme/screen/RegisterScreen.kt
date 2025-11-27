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
import com.example.fnx_huerto_hogar.ui.theme.viewModel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: UserViewModel = viewModel()
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

    // Navegar si el registro fue exitoso
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
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
                onRegionChange = viewModel::onRegionChange
            )

            Spacer(modifier = Modifier.height(24.dp))

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
                onClick = viewModel::register,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text(
                        text = "Registrarse",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
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
    onRegionChange: (String) -> Unit
) {
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

        // Campo Contraseña (FILA INDIVIDUAL)
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

        // Campo Confirmar Contraseña (FILA INDIVIDUAL)
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

        // Fila para comuna y región
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo Comuna
            OutlinedTextField(
                value = comuna,
                onValueChange = onComunaChange,
                label = { Text("Comuna") },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    focusedLabelColor = GreenPrimary,
                    cursorColor = GreenPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            )

            // Campo Región
            OutlinedTextField(
                value = region,
                onValueChange = onRegionChange,
                label = { Text("Región") },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    focusedLabelColor = GreenPrimary,
                    cursorColor = GreenPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}